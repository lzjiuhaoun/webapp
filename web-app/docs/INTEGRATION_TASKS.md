# INTEGRATION_TASKS.md — 集成里程碑

## 跨模块协同进度表

本文档仅关注集成测试点，不记录模块内部琐碎开发任务。

**版本：** v1.0.0

---

## 集成里程碑

| 里程碑 | 涉及模块 | 集成测试点 | 前置依赖 |
|--------|---------|-----------|---------|
| M1：用户认证链路 | auth, system | CAS登录成功 → Session创建 → 用户信息返回 | CAS服务器可用 |
| M2：资产发现链路 | db-assets, device, config | 新增设备 → 资产发现任务 → 盘点结果入库 | device 模块可用 |
| M3：敏感数据识别链路 | sensdata, dm-engine, db-assets | 选择数据库 → 敏感扫描 → 识别结果 → 统计汇总 | db-assets 有数据 |
| M4：脱敏策略下发链路 | strategy, instruction, device | 创建策略 → 封装指令 → 下发设备 → ACK确认 | device 已注册 |
| M5：风险分析聚合链路 | risk, db-assets, device, sensdata | 资产+设备+敏感数据 → 风险统计 → 风险等级 | M2, M3 完成 |
| M6：态势展示链路 | situation, risk, db-assets, device | 各维度数据聚合 → 图表展示 → 趋势分析 | M5 完成 |
| M7：审计策略链路 | audit, strategy, device | 创建审计规则 → 下发设备 → 审计日志采集 | device 已注册 |
| M8：告警通知链路 | system, server, risk | 风险事件 → 告警触发 → 多渠道通知 | server 已配置 |
| M9：报表导出链路 | report, situation, risk, sensdata | 数据聚合 → 模板渲染 → PDF/Excel导出 | M6 完成 |
| M10：备份恢复链路 | backup | 全量备份 → 增量备份 → 数据恢复 | — |
| M11：文件数据扫描链路 | filedata, sensdata, dm-engine | 文件目录扫描 → 敏感识别 → 结果入库 | — |
| M12：预测评估链路 | predict, risk, situation | 历史数据 → 趋势预测 → 安全评估 | M6 完成 |

## 集成测试环境要求

| 组件 | 最低要求 |
|------|---------|
| MySQL | 可连接，端口 23306 |
| Elasticsearch | 可连接，端口 9200 |
| Kafka | 可连接，端口 9092 |
| Manager-Center | 可连接，端口 9090 |
| CAS 服务器 | 可连接（集成测试时）|

## 集成失败处理

| 场景 | 处理策略 |
|------|---------|
| 依赖模块未就绪 | 使用 Mock Service，标记集成点待补测 |
| 外部服务不可用 | 跳过集成测试点，记录到测试报告 |
| 数据不一致 | 以提供方的 EXPORTS.md 为准，调用方适配 |
| 接口不兼容 | 检查 GLOBAL_CONTRACTS.md 版本号，回退到兼容版本 |
