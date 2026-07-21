# DEPENDENCIES.md — report 报表模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| situation | `OverallSituationService` — 态势数据 | >= v1.0.0 |
| risk | 各风险Service — 风险数据 | >= v1.0.0 |
| sensdata | `SensDataSummaryService` — 敏感数据 | >= v1.0.0 |
| db-assets | `DbAssetsService` — 资产数据 | >= v1.0.0 |
| server | `FtpConfigService` — FTP上传 | >= v1.0.0 |

## 外部库依赖

| 库 | 用途 |
|----|------|
| org.apache.poi:poi-ooxml | Excel导出 |
| com.itextpdf:itextpdf | PDF生成 |
| com.itextpdf.tool:xmlworker | PDF HTML渲染 |
| com.itextpdf:itext-asian | PDF中文字体 |
| com.ankki:common-module | 公共工具类 |
