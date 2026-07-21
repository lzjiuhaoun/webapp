# INTERNAL_LOGIC.md — socket-comm 内部实现逻辑

> 本模块为通信库，不独立运行。

## Socket 服务端实现

- `ServerSocketService` 使用 Java Bio Socket，Accept 线程接受连接后交由线程池处理
- 每连接一线程，从连接读取数据后按 `\n` 拆分消息
- 消息按管道分隔格式 `messType|contentType|operType|operNum|uuids|timestampMill` 解析
- 解析后的消息交给 `AbstractMessHandler` 子类处理
- 异常连接自动关闭并重连

## Socket 客户端实现

- `SocketClient` 使用 Java Socket，支持 TCP NoDelay、SOTimeout
- `retryInitSocket()` 带重试机制（默认 2 次，间隔 3 秒）
- `MessageToServerSocket` 为静态工具类，从配置读取 manager-center 地址
- 发送时建立 TCP 连接，写入消息后关闭连接（短连接模式）

## 消息队列实现

- `SocketMessQueue` 基于 `LinkedBlockingQueue`
- `DefaultMessHandler` 收到消息后解析为 Map，放入静态队列
- 消费方调用 `SocketMessQueue.getQUEUES().take()` 阻塞获取消息
- 队列容量：默认无限制

## BeanUtil 单例

- `BeanUtil` 实现 ApplicationContextAware，通过 EnumSingleton 提供非 Spring Bean 上下文中获取 Bean 的能力
