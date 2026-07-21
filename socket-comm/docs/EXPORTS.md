# EXPORTS.md — socket-comm 出口契约

> 版本: 1.0.0

## 提供的服务/类

### 1. Socket 服务端

**包**: com.ankki.socketcomm.server

| 类 | 用途 |
|------|------|
| `ServerSocketService` | 线程模型 TCP 服务器，每连接一线程 |
| `AbstractMessHandler` | 消息处理器抽象类（子类实现具体处理逻辑） |
| `DefaultMessHandler` | 默认消息处理器 |

**核心方法**:
- `ServerSocketService.start(int port)` — 启动服务
- `ServerSocketService.setMessHandler(AbstractMessHandler handler)` — 设置消息处理器

**线程池配置**:
- corePoolSize: 6
- maxPoolSize: 6

### 2. Socket 客户端

**包**: com.ankki.socketcomm.client

| 类 | 用途 |
|------|------|
| `SocketClient` | Socket 客户端封装 |
| `MessageToServerSocket` | 静态工具类，发送消息到 manager-center |

**核心方法**:
- `SocketClient.initSocket(host, port)` — 建立连接
- `SocketClient.retryInitSocket(host, port, retriesCount)` — 带重试的连接
- `SocketClient.sendMessage(message)` — 发送消息
- `MessageToServerSocket.send(message)` — 发送消息到配置的目标地址

### 3. 消息队列

**包**: com.ankki.socketcomm.server

| 类 | 用途 |
|------|------|
| `SocketMessQueue` | 基于 LinkedBlockingQueue 的消息接收队列 |

**核心方法**:
- `getQUEUES()` — 获取静态消息队列
- `take()` — 阻塞消费消息

### 4. 配置

**包**: com.ankki.socketcomm.config

| 类 | 用途 |
|------|------|
| `SystemConfig` | 客户端配置 (tcpNoDelay, soTimeout, retriesCount, 线程池) |
| `ServerSocketPropertites` | 服务端配置 (host, port, backLog, timeout, receiveBufferSize, reuseAddress) |

**默认配置**:
- manager-center 地址: `localhost:9090`
- TCP No Delay: `true`
- SO Timeout: `2000000ms`
- Retries Count: `2`

### 5. 消息格式

**管道分隔格式**:
```
messType|contentType|operType|operNum|uuids|timestampMill
```

**状态码**:
- 0: 成功
- 1: Socket 连接失败
- 2: 验证失败 (IP 非 127.0.0.1)
