# EXPORTS.md — report 报表模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/report-manager` | GET/POST | 报表管理与导出 |
| `/bi-task` | GET/POST/PUT/DELETE | BI任务管理 |
| `/data-maintain` | GET/POST | 数据维护 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `ReportExportService` | 报表导出 | system |
| `BiTaskService` | BI任务管理 | system |
| `DataMaintainService` | 数据维护 | system |

## 数据模型

| 实体 | 说明 |
|------|------|
| BiTask | BI任务 |
| BiTaskResult | BI任务结果 |
| ReportExport | 报表导出记录 |
| ExportReportRecord | 导出记录 |
| DailyStatistics | 每日统计 |
| DataMaintain | 数据维护记录 |
