# ACCEPTANCE.md — socket-comm 模块验收

## 验收标准

### T1: Socket 服务端
- [ ] ServerSocketService 可在指定端口启动
- [ ] 可接受多个客户端并发连接
- [ ] 线程池（core 6, max 6）正常工作
- [ ] 消息按管道分隔格式正确解析

### T2: Socket 客户端
- [ ] SocketClient 可建立连接
- [ ] retryInitSocket 带重试机制（默认 2 次，间隔 3 秒）
- [ ] MessageToServerSocket 可发送消息到目标地址
- [ ] 发送失败时自动重试

### T3: 消息队列
- [ ] SocketMessQueue 可阻塞等待消息
- [ ] 多消费者并发消费正常
- [ ] 队列容量无限制

### T4: 消息处理器
- [ ] AbstractMessHandler 子类可自定义处理逻辑
- [ ] DefaultMessHandler 解析管道分隔消息并放入队列
