# DEPENDENCIES.md — instruction 指令模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| device | `SafetyDevConfigService` — 设备通信信息 | >= v1.0.0 |
| strategy | `DesensStrategyService` — 策略数据 | >= v1.0.0 |

## 外部库依赖

| 库 | 用途 |
|----|------|
| com.ankki:socket-comm-spring-boot-starter | 跨进程指令通信 |
| com.ankki:common-module | 公共工具类 |
