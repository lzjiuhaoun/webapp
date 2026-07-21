# EXPORTS.md — sensdata 敏感数据模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/sensdata/sens-type` | GET/POST/PUT/DELETE | 敏感类型管理 |
| `/sensdata/task` | GET/POST | 敏感数据发现任务 |
| `/sensdata/summary` | GET | 敏感数据统计汇总 |
| `/sensdata/dictionary` | GET/POST/PUT/DELETE | 字典管理 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `SensDataFindService` | 敏感数据发现任务 | risk, situation |
| `SensDataSummaryService` | 敏感数据统计汇总 | risk, situation, report |
| `SensitiveService` | 敏感类型管理 | strategy |
| `DictionaryService` | 字典管理 | dm-engine |

## 数据模型

| 实体 | 说明 |
|------|------|
| SensType | 敏感类型定义 |
| SensTypeDetail | 敏感类型详情 |
| FindTask | 发现任务 |
| FindRecord | 发现记录 |
| FindInfo | 发现信息 |
| FindDetails | 发现详情 |
| FindDetailsSum | 发现详情汇总 |
| DataDictionary | 数据字典 |
| Cell | 数据单元 |
