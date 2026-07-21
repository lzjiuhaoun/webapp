# INTERNAL_LOGIC.md — sensdata 敏感数据模块

## 核心流程

### 敏感数据发现
```
创建发现任务 → SensDataFindService
                    ↓
      Quartz调度 → SensDataJobThread
                    ↓
      连接目标数据库 → 逐表逐字段扫描
                    ↓
      使用 dm-engine 引擎识别 → 匹配敏感类型
                    ↓
      识别结果入库 → FindDetails
                    ↓
      统计汇总 → FindDetailsSum
```

### 敏感类型识别
- 基于 `dm/algorithm/check/impl/` 下的35+种检查器
- 包括：身份证、手机号、银行卡、邮箱、地址、IP地址等
- 正则表达式 + 规则引擎组合识别

### 字典管理
- `ScheduledUpdateDictionaryThread` 定期更新识别字典
- 支持省份、城市、姓氏等基础字典
- 字典文件在 `resources/dictionary/*.sql`
