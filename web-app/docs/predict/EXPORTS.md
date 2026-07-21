# EXPORTS.md — predict 态势预测模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/predict/situation` | GET | 态势预测数据 |
| `/predict/assessment` | GET/POST | 安全评估 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `SituationAssessmentService` | 安全评估管理 | situation, report |

## 数据模型

| 实体 | 说明 |
|------|------|
| ActualData | 实际数据 |
| PredictionData | 预测数据 |
| SituationAssessment | 态势评估 |
| SituationData | 态势数据 |
