# CLAUDE.md — socket-comm 模块指令

## 模块角色

进程间 TCP Socket 通信库，提供客户端/服务端封装和消息处理机制。不作为独立进程部署，仅作为 Maven 依赖。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.5.15 | 基础框架 |
| Java Socket API | — | 网络通信 |
| ThreadPoolExecutor | — | 线程池 |
| Lombok | — | 代码生成 |

## Source of Truth

- 源代码: ../src/main/java/com/ankki/socketcomm/
- 配置: ../src/main/resources/application.yml
- POM: ../pom.xml
- 主类: com.ankki.socketcomm.SocketCommApplication (仅用于依赖注入)

## 核心职责

1. **Socket 服务端**: ServerSocketService 线程模型服务器
2. **Socket 客户端**: SocketClient + MessageToServerSocket 静态工具
3. **消息队列**: SocketMessQueue 阻塞队列消费
4. **消息处理器**: AbstractMessHandler / DefaultMessHandler 回调处理

## 被以下模块引用

kafka-client, web-app, manager-center
