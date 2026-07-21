# CLAUDE.md — risk 风险分析模块

## 模块使命

聚合多维度安全风险数据：攻击风险、漏洞风险、运维风险、数据访问风险和总体风险统计，为态势感知和报表提供数据支撑。

## 技术栈

- MyBatis + MySQL
- Spring Data Elasticsearch
- 后台线程（数据聚合）

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
├── controller/risk/
│   ├── AttackRiskController           ← 攻击风险
│   ├── VulnerabilityRiskController    ← 漏洞风险
│   ├── OperationRiskController        ← 运维风险
│   ├── OverallRiskController          ← 总体风险
│   └── DataAccessRiskController       ← 数据访问风险
├── service/
│   ├── AttackRiskService/Impl
│   ├── VulnerabilityRiskService/Impl
│   ├── OperationRiskService/Impl
│   ├── OverallRiskService/Impl
│   └── DataAccessRiskService/Impl
├── dao/risk/
│   ├── AttackRiskMapper
│   ├── VulnerabilityRiskMapper
│   ├── SystemOperationRiskMapper
│   ├── UserBehaviorMapper
│   └── StatSensMapper
└── model/risk/
    ├── AttackOverview, AttackDb, AttackIp, AttackLev, AttackTool, AttackType
    ├── AttackRiskAccess, DbTypeAssetsModel, EngineProcess
    ├── SystemOperationStatus, SystemOpenPort, SystemRiskAccess, SystemRiskType
    ├── UserBehavior, VulneBaseModel, VulneTrendModel
    ├── AccessRisk, ApiTableRelatio, SensFiled, SecurityModel
    ├── AasStatSens, ApiStatSens
    └── UserBehaviorSankeyDTO
```
