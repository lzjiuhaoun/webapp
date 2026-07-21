# CLAUDE.md — filedata 文件数据模块

## 模块使命

管理文件型数据源：文件目录扫描、模板配置、文件详情管理和敏感数据发现。

## 技术栈

- MyBatis + MySQL
- Quartz 定时任务（文件扫描）

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
├── controller/filedata/
│   ├── FileDataInfoController           ← /filedata-info
│   ├── FileDataTemplateController       ← /filedata-template
│   ├── FileDirectoryDetailController    ← /file-directory
│   └── FileSourceConfigController       ← /file-config
├── service/
│   ├── FileDataInfoService/Impl
│   ├── FileDataTemplateService/Impl
│   ├── FileDirectoryDetailService/Impl
│   ├── FileSourceConfigService/Impl
│   ├── FileDataDetailGetData
│   └── FileDataUpdateThread
├── dao/filedata/
│   ├── FileDataInfoMapper, FileDataTemplateMapper
│   ├── FileDataTemplateFieldMapper, FileDataDetailMapper
│   ├── FileDirectoryDetailMapper
│   └── FileSourceConfigMapper
└── model/filedata/
    ├── FileDataInfo, FileDataTemplate
    ├── FileDataTemplateField, FileDataDetail
    ├── FileDirectoryDetail, FileSourceConfig
    └── FileDataContentItem
```
