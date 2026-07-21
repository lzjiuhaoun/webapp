# INTERNAL_LOGIC.md — report 报表模块

## 核心流程

### 报表导出
```
用户选择报表 → ReportExportService
                    ↓
      聚合数据 → 从 situation/risk/sensdata 等模块获取
                    ↓
      格式化 → Excel(POI) / PDF(iText)
                    ↓
      文件生成 → 返回下载流
```

### BI任务
- `BiTaskThread` 执行任务
- `BiTaskAllocationThread` 分配子任务
- `GetBiTaskResult` 收集结果
- 支持多种报表类型：日报、周报、月报、自定义周期

### 定时报表线程
- `ReportDaily` — 每日报表
- `ApiAuditDaily` — API审计日报
- `StatisticalDataFlowInfo` — 数据流转统计
- `TotalStatisticalRisks` — 风险统计汇总
- `UserBehaviorReport` — 用户行为报告
- `DbClassLevelStatistics` — 数据库分类定级统计
