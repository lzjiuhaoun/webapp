# TASKS.md — manager-center 局部任务书

## 阶段 1: Socket 服务器框架

### 任务 1.1: ServerSocket 配置
- [ ] `GlobalConfiguration.serverSocket()` Bean 创建
- [ ] 绑定 `localhost:9090`
- [ ] `ServerSocketPropertites` 配置属性映射
- [ ] setSoTimeout / setReceiveBufferSize / setReuseAddress

### 任务 1.2: 消息接收循环
- [ ] `ServerSocketService` 实现 `Runnable`
- [ ] `while(true)` accept 循环
- [ ] 每次连接创建 `ManagementCenterMessHandler`
- [ ] 提交到线程池执行

### 任务 1.3: 线程池初始化
- [ ] `initThreadPool()` 创建 `ThreadPoolExecutor`
- [ ] core=6, max=6, keepAlive=1800s, queue=50
- [ ] ThreadFactory 命名规则: `ServerSocketService socket worker thread:N`
- [ ] CallerRunsPolicy 拒绝策略

## 阶段 2: 消息处理

### 任务 2.1: 消息读取与验证
- [ ] `AbstractMessHandler` 抽象基类
- [ ] `isLegalSocket()` 仅允许 127.0.0.1
- [ ] `readMess()` 读取 UTF 字符串
- [ ] `responseMess(byte)` 返回状态码

### 任务 2.2: 消息解析
- [ ] `split("\\|")` 分割管道分隔消息
- [ ] `messageType` 和 `contentType` 提取
- [ ] 异常消息记录日志

## 阶段 3: 路由转发

### 任务 3.1: 路由表查询
- [ ] `MessageRoutingMapper.selectObjByParams()`
- [ ] `MessageRouting` 模型定义
- [ ] MessageRoutingMapper.xml SQL 映射

### 任务 3.2: 特殊消息处理
- [ ] `messageType == 4 && contentType == 1`
- [ ] 执行 Shell 命令: `cd /home/simp/conf/client; sh start_all_service.sh`
- [ ] `LinuxUtil.executeLinuxCommand()`

### 任务 3.3: 消息转发
- [ ] `SocketClient.initSocket(host, port)`
- [ ] `SocketClient.sendMessage(message)`
- [ ] `SocketClient.releaseResource()`
- [ ] 广播转发（N 条匹配）
- [ ] 转发失败静默处理

## 阶段 4: 工具类

### 任务 4.1: BeanUtil
- [ ] `BeanUtil` 实现 `ApplicationContextAware`
- [ ] `EnumSingleton` 单例模式
- [ ] 非 Spring Bean 上下文中获取 Bean

### 任务 4.2: LinuxUtil
- [ ] `executeLinuxCommand()` 执行 Shell 命令
- [ ] 使用 Runtime.getRuntime().exec()
- [ ] 输出编码 GB2312

## 阶段 5: 服务注册

### 任务 5.1: Zookeeper 注册
- [ ] `ManagerCenterApplication.run()` 实现 `CommandLineRunner`
- [ ] 创建 `ZookeeperClient(serviceName, ip:port)`
- [ ] 注册 `managercenter` 到 Zookeeper
- [ ] 创建临时节点（进程退出自动删除）

## 阶段 6: 打包

- [ ] ProGuard 混淆配置
- [ ] AppAssembler 打包
- [ ] tar.gz 打包
