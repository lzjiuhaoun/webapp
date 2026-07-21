# INTERNAL_LOGIC.md — manager-center 内部实现逻辑

> 本模块描述 manager-center 的内部实现细节。

## 一、启动流程

### 1. Spring Context 加载
- `@SpringBootApplication` 触发组件扫描
- `@MapperScan("com.ankki.managercenter.dao")` 注册 MyBatis Mapper
- `spring.factories` 自动配置 `GlobalConfiguration`

### 2. ServerSocket Bean 创建（GlobalConfiguration）
```java
@Bean
public ServerSocket serverSocket(ServerSocketPropertites props) {
    ServerSocket serverSocket = new ServerSocket();
    // 可选: setSoTimeout, setReceiveBufferSize, setReuseAddress
    if (props.getBackLog() != null) {
        serverSocket.bind(new InetSocketAddress(props.getHost(), props.getPort()), props.getBackLog());
    } else {
        serverSocket.bind(new InetSocketAddress(props.getHost(), props.getPort()));
    }
}
```
- 绑定 `localhost:9090`（来自 `application.yml`）
- `backLog` 默认系统值（50）

### 3. 主线程启动
```java
SpringApplication.run(ManagerCenterApplication.class, args);
// 获取 ServerSocketService Bean
ServerSocketService bean = BeanUtil.EnumSingleton.INSTANCE
    .getInstance().getBean(ServerSocketService.class);
new Thread(bean).start();  // 启动 accept 循环
```

### 4. CommandLineRunner 执行
```java
public void run(String... args) throws Exception {
    String ip = InetAddress.getByName(host).getHostAddress();
    ZookeeperClient zkcli = new ZookeeperClient(serviceName, ip + ":" + port);
    zkcli.connectZk();
    zkcli.registerServerStandalone();
}
```
- 注册 `managercenter` 到 Zookeeper，值为 `localhost:9090`
- 创建**临时节点**（进程退出自动删除）

### 5. Accept 循环启动（ServerSocketService.run）
```java
while (true) {
    Socket sc = serverSocket.accept();  // 阻塞等待
    messHandler = new ManagementCenterMessHandler(sc);
    threadPoolExecutor.submit(messHandler);
}
```

## 二、消息处理流程

### ManagementCenterMessHandler.call()
```java
public Object call() throws Exception {
    try {
        handleMess();    // 处理消息
    } finally {
        releaseSource(); // 资源清理
    }
    return null;
}
```

### handleMess() — 消息处理

```java
public void handleMess() throws Exception {
    if (!isLegalSocket()) {
        // 验证失败: 只接受 127.0.0.1
        return;
    }
    String message = readMess();       // 读取 UTF 字符串
    responseMess((byte) 0);            // 返回状态 0（成功）
    publishMess(message);              // 路由转发
}
```

### isLegalSocket() — IP 验证
- 检查 `socket.getInetAddress().getHostAddress()`
- 仅允许 `127.0.0.1`
- 非法 IP 不返回状态码（静默丢弃）

### readMess() — 消息读取
- 使用 `DataInputStream.readUTF()`
- 读取完整管道分隔字符串

### responseMess(byte code) — 状态响应
- 使用 `DataOutputStream.writeByte()`
- 写入单个字节状态码

## 三、消息路由逻辑

### publishMess() — 核心路由

```java
public void publishMess(String message) {
    // 1. 解析消息
    String[] mess = message.split("\\|");
    int messageType = Integer.parseInt(mess[0]);
    int contentType = Integer.parseInt(mess[1]);
    
    // 2. 特殊消息处理: 类型 4, 内容 1 → 重启所有服务
    if (messageType == 4 && contentType == 1) {
        String cmd = "cd /home/simp/conf/client;sh start_all_service.sh ";
        String[] cmdStr = {"/bin/sh", "-c", cmd};
        LinuxUtil.executeLinuxCommand(null, cmdStr);
        log.info("重启所有的组件与服务成功");
        return;  // 不查路由表，不转发
    }
    
    // 3. 查询路由表
    MessageRoutingMapper mapper = BeanUtil.EnumSingleton.INSTANCE
        .getInstance().getBean(MessageRoutingMapper.class);
    List<MessageRouting> messageRoutings = 
        mapper.selectObjByParams(messageType, contentType);
    
    // 4. 无路由 → 丢弃
    if (messageRoutings.isEmpty()) {
        log.info("路由表中没有找到{}的路由信息！", message);
        return;
    }
    
    // 5. 转发到所有匹配目标（广播）
    messageRoutings.forEach(messageRouting -> {
        boolean isSuccess = socketClient.initSocket(
            messageRouting.getModuleServersocketHost(),
            messageRouting.getModuleServersocketPort()
        );
        if (isSuccess) {
            socketClient.sendMessage(message);       // 转发消息
            socketClient.releaseResource();           // 关闭连接
        }
        // 失败时静默记录日志，继续下一个路由
    });
}
```

## 四、Socket 客户端实现

### SocketClient — 一次性连接

```java
@Component
@Scope("prototype")
public class SocketClient {
    
    public boolean initSocket(String host, Integer port) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), soTimeout);
            socketPropertitesConfig();  // TCP NoDelay 等
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            log.error("", e);
            return false;  // 无重试
        }
    }
    
    public void sendMessage(String message) {
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error("", e);
        }
    }
    
    public void releaseResource() {
        try {
            if (dataOutputStream != null) dataOutputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
```

**关键特征**：
- 每次路由创建一个新 Socket 连接
- 发送后**立即关闭**（短连接模式）
- **无重试逻辑**（与 socket-comm 不同）
- `@Scope("prototype")` 保证线程安全

## 五、线程池实现

### ServerSocketService.initThreadPool()

```java
private void initThreadPool() {
    threadFactory = new ThreadFactory() {
        private int count = 1;
        public Thread newThread(Runnable r) {
            return new Thread(r, "ServerSocketService socket worker thread:" + count++);
        }
    };
    
    if (null == threadPoolExecutor || threadPoolExecutor.isShutdown()) {
        threadPoolExecutor = new ThreadPoolExecutor(
            coreSize,              // 6
            maxThreadSize,         // 6
            keepAliveTime,         // 1800 秒
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(blockQueueSize),  // 50
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
```

**特征**：
- core = max，线程数固定，永不回收（除非 shutdown）
- 队列满时 CallerRunsPolicy：由调用线程（accept 线程）执行
- 每次新连接时检查是否需要重新初始化

## 六、BeanUtil 单例模式

### EnumSingleton 访问 Spring Bean

```java
@Component
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext context;
    
    public static <T> T getBean(Class<T> clazz) {
        return EnumSingleton.INSTANCE.getInstance().getBean(clazz);
    }
    
    public static class EnumSingleton {
        public static final EnumSingleton INSTANCE = new EnumSingleton();
        private EnumSingleton() {}
        public BeanUtil getInstance() {
            return BeanUtil;
        }
        public <T> T getBean(Class<T> clazz) {
            return context.getBean(clazz);
        }
    }
}
```

用途：在非 Spring Bean 上下文中获取 Bean（如 `AbstractMessHandler` 继承类）。

## 七、LinuxUtil 命令执行

```java
public final class LinuxUtil {
    public static String executeLinuxCommand(String cmdStr, String[] cmdStrArr) {
        String result = null;
        try {
            Process process = Runtime.getRuntime().exec(cmdStrArr);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), "GB2312")
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
```

- 使用 `Runtime.getRuntime().exec()` 执行 Shell 命令
- 输出编码: GB2312
- 用于执行系统重启脚本

## 八、安全约束

### IP 白名单
- `isLegalSocket()` 仅允许 `127.0.0.1`
- 远程连接被静默丢弃
- 这是一个显著的限制：manager-center 只能接受本地连接

### 无 TLS/SSL
- Socket 通信是明文 TCP
- 无加密，无身份认证（除 IP 白名单外）

## 九、异常处理

### 无重试机制
- `SocketClient.initSocket()` 失败返回 `false`，无重试
- 路由转发失败的消息被丢弃

### 无死信队列
- 无路由匹配的消息：记录日志，丢弃
- 转发失败的消息：记录日志，丢弃

### 资源清理
- `releaseSource()` 在 `finally` 块中保证执行
- 关闭 `DataInputStream`, `DataOutputStream`, `Socket`

### Accept 循环容错
```java
while (true) {
    try {
        Socket sc = serverSocket.accept();
        // ...
    } catch (IOException e) {
        log.error("", e);  // 记录日志，继续循环
    }
}
```

## 十、关闭行为

### 无显式关闭钩子
- 无 `@PreDestroy`
- 无 `DisposableBean`
- 无 shutdown hook

### 关闭时的缺口
1. `ServerSocket` 永不关闭（无 `serverSocket.close()`）
2. `ThreadExecutor` 永不 shutdown（无 `shutdown()` 调用）
3. Zookeeper 节点不显式删除（依赖 `EPHEMERAL` 自动清理）
4. Socket 连接可能 abrupt 终止

### Zookeeper 行为
- 注册 `EPHEMERAL` 节点
- JVM 进程终止时，ZooKeeper 自动检测 session 过期并删除节点
- 其他服务通过 `WatchCom` 监听该路径将收到通知
