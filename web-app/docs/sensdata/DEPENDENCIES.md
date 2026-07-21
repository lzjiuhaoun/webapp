# DEPENDENCIES.md — sensdata 敏感数据模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| db-assets | `DbAssetsService` — 数据库资产信息 | >= v1.0.0 |
| dm-engine | 脱敏算法引擎 — 敏感识别 | >= v1.0.0 |

## 外部库依赖

| 库 | 用途 |
|----|------|
| org.springframework.data:spring-data-elasticsearch | ES检索 |
| com.ankki:common-module | 公共工具类 |
