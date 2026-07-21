# DEPENDENCIES.md — system 系统管理模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| server | `MailConfigService`, `SmsConfigService` 等通知渠道 | >= v1.0.0 |
| db-assets | `DbAssetsService` — 资产统计 | >= v1.0.0 |
| device | `DevAlarmHistoryService` — 设备告警 | >= v1.0.0 |

## 外部库依赖

| 库 | 用途 |
|----|------|
| org.springframework.boot:spring-boot-starter-quartz | 定时任务 |
| com.ankki:socket-comm-spring-boot-starter | 跨进程通信 |
| com.ankki:common-module | 公共工具类 |
| org.bouncycastle:bcprov-jdk15to18 | License签名校验 |
| io.jsonwebtoken:jjwt-impl | Token处理 |
