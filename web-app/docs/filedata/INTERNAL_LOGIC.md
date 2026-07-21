# INTERNAL_LOGIC.md — filedata 文件数据模块

## 核心流程

### 文件扫描
```
配置文件数据源 → FileSourceConfigService
                    ↓
      后台线程 → FileDataUpdateThread
                    ↓
      扫描目录 → 读取文件 → FileDataDetailGetData
                    ↓
      解析文件内容 → 模板匹配
                    ↓
      敏感数据识别 → 通知 sensdata 模块
```

### 模板匹配
- 使用 FileDataTemplate 定义文件结构
- FileDataTemplateField 定义字段位置和类型
- 支持 CSV、TXT、Excel 等格式
