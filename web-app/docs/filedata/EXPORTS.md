# EXPORTS.md — filedata 文件数据模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/filedata-info` | GET/POST/PUT/DELETE | 文件数据信息管理 |
| `/filedata-template` | GET/POST/PUT/DELETE | 文件模板管理 |
| `/file-directory` | GET/POST/PUT/DELETE | 文件目录详情 |
| `/file-config` | GET/POST/PUT/DELETE | 文件数据源配置 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `FileSourceConfigService` | 文件数据源配置 | sensdata |
| `FileDataInfoService` | 文件数据信息管理 | sensdata |
| `FileDirectoryDetailService` | 文件目录详情 | sensdata |
| `FileDataTemplateService` | 文件模板管理 | sensdata |

## 数据模型

| 实体 | 说明 |
|------|------|
| FileSourceConfig | 文件数据源配置 |
| FileDataInfo | 文件数据信息 |
| FileDataTemplate | 文件模板 |
| FileDataTemplateField | 模板字段 |
| FileDirectoryDetail | 文件目录详情 |
| FileDataDetail | 文件数据详情 |
