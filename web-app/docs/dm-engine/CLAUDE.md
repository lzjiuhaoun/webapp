# CLAUDE.md — dm-engine 脱敏引擎模块

## 模块使命

提供数据脱敏算法引擎：敏感类型识别（检查）、数据脱敏（映射/随机/降维/可逆/分区），支持35+种敏感数据类型。

## 技术栈

- 纯Java算法库
- 无外部框架依赖

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
dm/
├── InitProject.java                    ← 引擎初始化
├── model/
│   ├── DataDictionary.java             ← 数据字典
│   ├── DataDictionaryDetail.java       ← 字典详情
│   ├── DesenRule.java                  ← 脱敏规则
│   ├── SensType.java                   ← 敏感类型
│   └── SensTypeDetail.java             ← 敏感类型详情
├── mapper/
│   ├── AlgorithmMapper.java
│   └── AddressMapper.java
├── util/
│   ├── CharConstant.java
│   ├── DateTimeValidator.java
│   ├── DictionaryUtil.java
│   └── StringUtil.java
└── algorithm/
    ├── AlgorithmEntrance.java          ← 算法入口
    ├── model/ (AddressCityModel等地址模型)
    ├── constant/ (算法常量)
    ├── util/ (算法工具类 + internal/内部工具)
    └── process/
        ├── check/                      ← 敏感识别检查
        │   ├── AlgorithmCheck.java     ← 检查接口
        │   └── impl/                   ← 35+检查实现
        ├── mapping/                    ← 映射脱敏
        │   ├── BaseMapping.java        ← 映射接口
        │   └── impl/                   ← 29+映射实现
        ├── random/                     ← 随机脱敏
        │   ├── BaseRandom.java         ← 随机接口
        │   └── impl/                   ← 29+随机实现
        ├── reduction/                  ← 降维脱敏
        │   ├── BaseReduction.java      ← 降维接口
        │   └── impl/                   ← 29+降维实现
        ├── reversible/                 ← 可逆脱敏
        │   ├── BaseReversible.java     ← 可逆接口
        │   └── impl/                   ← 29+可逆实现
        └── partition/                  ← 分区脱敏
            ├── BasePartition.java      ← 分区接口
            └── impl/                   ← 29+分区实现
```
