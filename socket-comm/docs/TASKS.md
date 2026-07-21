# TASKS.md — socket-comm 局部任务书

## 开发任务

### 阶段 1: Socket 服务端
- [ ] ServerSocketService 基础框架
- [ ] Accept 线程与线程池
- [ ] 连接读取与消息拆分
- [ ] AbstractMessHandler 回调接口

### 阶段 2: Socket 客户端
- [ ] SocketClient 连接管理
- [ ] retryInitSocket 重试机制
- [ ] MessageToServerSocket 静态工具
- [ ] 发送消息与重试

### 阶段 3: 消息队列
- [ ] SocketMessQueue LinkedBlockingQueue 封装
- [ ] 消息入队/出队
- [ ] 默认消息处理器 DefaultMessHandler

### 阶段 4: 配置
- [ ] SystemConfig 配置加载
- [ ] ServerSocketPropertites 配置属性
- [ ] application.yml 配置项
