# CLAUDE.md — report 报表模块

## 模块使命

提供报表导出、BI任务管理和数据维护能力。支持Excel和PDF格式导出，定时生成统计数据报表。

## 技术栈

- MyBatis + MySQL
- Apache POI（Excel）
- iText PDF
- Quartz 定时任务（BI任务）
- Spring Data Elasticsearch（审计日志搜索）

## 文档索引

| 文档 | 路径 |
|------|------|
| 出口契约 | `EXPORTS.md` |
| 内部逻辑 | `INTERNAL_LOGIC.md` |
| 依赖声明 | `DEPENDENCIES.md` |
| 测试策略 | `TESTING.md` |
| 任务书 | `TASKS.md` |
| 验收标准 | `ACCEPTANCE.md` |

## 代码结构

```
com.ankki.webapp/
├── controller/export/
│   └── ReportExportController     ← /report-manager
├── controller/bi/
│   └── BiTaskController           ← BI任务管理
├── controller/datamaintenance/
│   └── DataMaintainController     ← /data-maintain
├── service/
│   ├── ReportExportService/Impl
│   ├── BiTaskService/Impl
│   ├── DataMaintainService/Impl
│   ├── BiTaskThread, BiTaskAllocationThread, GetBiTaskResult
│   ├── ApiAuditDaily, DbClassLevelStatistics
│   ├── DealEventDataReportTaskThread, ReportDaily
│   ├── StatisticalDataFlowInfo, TotalStatisticalRisks
│   └── UserBehaviorReport
├── dao/bi + data/
│   ├── BiTaskMapper, BiTaskResultMapper
│   ├── DailyStatisticsMapper, DataBackUp
│   ├── TemplateMapper, StatisticResultMapper
│   └── DataMainTainMapper
└── model/bi + data + datamaintenance/
    ├── BiTask, BiTaskResult, ExportReportRecord
    ├── ReportInformation, ReportExport
    ├── DailyStatistics, DataFlowBackground
    ├── DataMaintain, DiskInfo
    └── 大量VO和Export类
```

## 导出工具

```
com.ankki.webapp.util/
├── ExcelUtil.java
├── PdfUtil.java
├── ReportExportUtil.java
├── HtmlUtil.java
├── ExportAndImportUtil.java
├── OneClickImportExportUtil.java
├── ExportTableUtil.java
├── ExcelEncryptionUtil.java
└── PdfReportHeaderFooter.java
```
