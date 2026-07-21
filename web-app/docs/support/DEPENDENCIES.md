# DEPENDENCIES.md — support 支撑模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| device | `SafetyDevConfigService` — 设备信息(拓扑) | >= v1.0.0 |
| db-assets | `DbAssetsService` — 资产信息(拓扑) | >= v1.0.0 |
| system | `SystemAlarmService` — 告警推送 | >= v1.0.0 |

## 外部库依赖

| 库 | 用途 |
|----|------|
| org.springframework.boot:spring-boot-starter-websocket | WebSocket |
| org.springframework.data:spring-data-elasticsearch | ES搜索 |
| net.sf.ehcache:ehcache | 缓存 |
| com.ankki:socket-comm-spring-boot-starter | Socket通信 |
| com.ankki:common-module | 公共工具类 |
