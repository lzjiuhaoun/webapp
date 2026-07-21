# DEPENDENCIES.md — risk 风险分析模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| db-assets | `DbAssetsService` — 资产数据 | >= v1.0.0 |
| device | `CmDevService`, `VsDevService` — 设备数据 | >= v1.0.0 |
| sensdata | `SensDataFindService` — 敏感数据 | >= v1.0.0 |

## 外部库依赖

| 库 | 用途 |
|----|------|
| org.springframework.data:spring-data-elasticsearch | ES查询 |
| org.elasticsearch:elasticsearch | ES客户端 |
| com.ankki:common-module | 公共工具类 |
