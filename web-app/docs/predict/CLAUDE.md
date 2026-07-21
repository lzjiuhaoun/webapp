# CLAUDE.md — predict 态势预测模块

## 模块使命

提供安全态势预测和安全评估能力：基于历史数据的趋势预测和安全评分评估。

## 技术栈

- MyBatis + MySQL
- Spring Data Elasticsearch

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
├── controller/predict/
│   ├── SituationPredictController    ← 态势预测
│   └── SituationAssessmentController ← 安全评估
├── service/
│   └── SituationAssessmentService/Impl
├── dao/predict/
│   ├── ActualDataMapper
│   ├── PredictionDataMapper
│   ├── SituationAssessmentMapper
│   └── SituationDataMapper
└── model/predict/
    ├── ActualData
    ├── PredictionData
    ├── SituationAssessment
    └── SituationData
```
