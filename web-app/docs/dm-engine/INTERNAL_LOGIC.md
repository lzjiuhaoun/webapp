# INTERNAL_LOGIC.md — dm-engine 脱敏引擎模块

## 核心流程

### 敏感识别
```
原始数据 → AlgorithmEntrance.check()
               ↓
      AlgorithmFactory 匹配检查器
               ↓
      检查器(如IdNumberCheck) → 正则+规则验证
               ↓
      返回敏感类型 + 匹配结果
```

### 脱敏处理
```
敏感数据 + 脱敏算法 → AlgorithmEntrance.desensitize()
                          ↓
          根据算法类型选择处理器
          ├── Mapping → BaseMapping实现
          ├── Random → BaseRandom实现
          ├── Reduction → BaseReduction实现
          ├── Reversible → BaseReversible实现
          └── Partition → BasePartition实现
                          ↓
          返回脱敏结果
```

### 内部工具类
- `DesensitizedArrayUtil` — 数组脱敏
- `DesensitizedChineseUtil` — 中文脱敏
- `DesensitizedCommonUtil` — 通用脱敏
- `DesensitizedDateUtil` — 日期脱敏
- `DesensitizedFloatUtil` — 浮点数脱敏
- `DesensitizedIntegerUtil` — 整数脱敏
- `DesensitizedListUtil` — 列表脱敏
- `DesensitizedStringUtil` — 字符串脱敏

### 字典管理
- `DictionaryUtil` 加载字典数据
- 字典来源：`resources/dictionary/*.sql`
- 省份、城市、姓氏等基础字典
