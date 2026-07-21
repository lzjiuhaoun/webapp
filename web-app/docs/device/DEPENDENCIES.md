# DEPENDENCIES.md — device 安全设备模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| instruction | `InstructionService` — 指令下发 | >= v1.0.0 |
| system | `SystemAlarmService` — 告警通知 | >= v1.0.0 |
| strategy | `DesensStrategyService` — 脱敏策略 | >= v1.0.0 |
| db-assets | `DbAssetsService` — 数据库资产信息 | >= v1.0.0 |

## 外部库依赖

| 库 | 用途 |
|----|------|
| com.ankki:socket-comm-spring-boot-starter | 跨进程通信 |
| org.springframework.kafka | Kafka消息队列 |
| com.ankki:common-module | 公共工具类 |
