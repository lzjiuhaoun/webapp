# DEPENDENCIES.md — strategy 策略管理模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| instruction | `InstructionService` — 指令下发 | >= v1.0.0 |
| device | `SafetyDevConfigService` — 设备信息 | >= v1.0.0 |
| db-assets | `DbAssetsService` — 数据库信息 | >= v1.0.0 |
| dm-engine | 脱敏算法引擎 | >= v1.0.0 |
| sensdata | `SensitiveService` — 敏感类型 | >= v1.0.0 |

## 外部库依赖

| 库 | 用途 |
|----|------|
| com.ankki:common-module | 公共工具类 |
