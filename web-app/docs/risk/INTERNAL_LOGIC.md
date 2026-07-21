# INTERNAL_LOGIC.md — risk 风险分析模块

## 核心流程

### 风险数据聚合
```
db-assets 资产数据 → risk Service
device 设备数据       →   ↓ 聚合计算
sensdata 敏感数据     →   ↓
                        ↓
              按维度统计(攻击/漏洞/运维/访问)
                        ↓
              写入风险统计表
                        ↓
              situation / report 模块读取
```

### 攻击风险分析
- 数据源：CM设备采集数据、VS防火墙拦截数据
- 分析维度：攻击IP、攻击类型、攻击工具、攻击目标数据库
- 风险等级：高/中/低（基于频率和严重程度）

### 用户行为分析
- `UserBehavior` 模型
- `@Scheduled(cron = "${userBehaviorCron}")` 每10分钟分析
- 通过 `UserBehaviorReport` 线程生成行为报告

### 风险数据预处理
- `ReportAssetRisk`, `ReportMainStats`, `ReportRiskCount` 等预处理服务
- 将原始数据转换为报表友好格式
