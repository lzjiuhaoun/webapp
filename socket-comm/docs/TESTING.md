# TESTING.md — socket-comm 测试策略

## 测试工具链

- JUnit 4 + Spring Boot Test
- Mockito（Mock 网络依赖）

## 测试目录结构

```
src/test/java/com/ankki/socketcomm/
├── server/         # 服务端测试
├── client/         # 客户端测试
└── config/         # 配置测试
```

## Mock 边界

- 网络 Socket：使用 localhost 回环测试
- 消息处理器：Mock AbstractMessHandler

## 测试重点

1. ServerSocketService 可启动并接受连接
2. MessageToServerSocket 可发送消息到目标服务器
3. SocketMessQueue 可阻塞消费消息
4. 管道分隔消息解析正确性
5. 多线程并发处理能力
6. SocketClient 重试机制
