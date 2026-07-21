# CLAUDE.md — manager-center 模块指令

## 模块角色

进程间 Socket 消息路由转发枢纽。接收来自任意模块的 TCP Socket 消息，查询 MySQL `message_routing` 路由表，将消息转发到目标模块的 Socket 服务器。独立 JVM 进程部署。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| JDK | 1.8 | 运行环境 |
| Spring Boot | 2.5.15 | 基础框架 |
| MyBatis | 3.5.10 | MySQL ORM |
| Druid | 1.2.4 | 数据库连接池 |
| MySQL Connector | 8.2.0 | MySQL 驱动 |
| DM 达梦驱动 | 8.1.3.62 | 国产数据库（备用） |
| Netty | 4.1.x | 网络库 |
| Zookeeper | 3.8.4 | 服务注册 |
| Logback | 1.2.13 | 日志框架 |
| common-module | 0.0.1-SNAPSHOT | Zookeeper 客户端 |

## Source of Truth

- 源代码: ../src/main/java/com/ankki/managercenter/
- 配置: ../src/main/resources/application.yml
- 数据库映射: ../src/main/resources/mapper/MessageRoutingMapper.xml
- POM: ../pom.xml
- 主类: com.ankki.managercenter.ManagerCenterApplication

## 核心职责

1. **Socket 消息接收**：监听 9090 端口，接收管道分隔格式的 Socket 消息
2. **消息路由转发**：根据 `messageType` + `contentType` 查询 MySQL 路由表，转发到目标模块
3. **系统重启指令**：特殊消息（类型 4，内容 1）触发全系统重启脚本
4. **服务注册**：启动时注册 `managercenter` 到 Zookeeper

## 运行配置

- JVM: `-server -Xmx1G -Xms1G` + verbose GC
- TCP 端口: 9090（绑定 localhost）
- ZK 服务名: `managercenter`
- 线程池: core=6, max=6, keepAlive=1800s, queue=50
- 日志: `/data/logs/manager-center/`
- 打包: ProGuard 混淆 + AppAssembler + tar.gz
