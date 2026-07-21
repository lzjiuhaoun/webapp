# INTERNAL_LOGIC.md — predict 态势预测模块

## 核心流程

### 趋势预测
```
历史数据(risk/situation) → SituationData
                               ↓
        趋势算法 → 预测未来风险趋势
                               ↓
        预测结果 → PredictionData
                               ↓
        前端展示趋势对比图
```

### 安全评估
- 基于多维度数据：风险、资产、设备、敏感数据
- 加权计算安全评分
- 生成评估报告
