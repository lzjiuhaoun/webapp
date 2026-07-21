# CLAUDE.md — daemon 模块指令

## 模块角色

守护进程：通过 Zookeeper Watch 机制监控平台所有其他进程的健康状态，检测到异常时自动重启故障服务。独立 JVM 进程部署。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.5.15 | 基础框架 |
| Zookeeper | 3.8.4 | 服务监控 |
| Netty | 4.1.x | 网络库 |
| FastJSON | — | JSON 处理 |
| Lombok | — | 代码生成 |

## Source of Truth

- 源代码: ../src/main/java/com/ankki/daemon/
- 配置: ../src/main/resources/application.properties
- POM: ../pom.xml
- 主类: com.ankki.daemon.DaemonApplication

## 核心职责

1. **服务监控**: 通过 Zookeeper Watch 监控所有注册服务
2. **异常检测**: 节点消失表示服务宕机
3. **自动恢复**: 执行重启脚本恢复服务
4. **系统监控**: Linux CPU/内存/网络监控

## 运行配置

- JVM: `-Xmx1G -Xms1G` + verbose GC
- ZK 服务名: `daemon`
- 监控服务: reportengine, syslogserver, webapp, managercenter, kafkaclient, kafkaclient1~5
- 打包: ProGuard 混淆 + AppAssembler
