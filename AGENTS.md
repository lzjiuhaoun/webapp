# CLAUDE.md — 系统宪法

## 项目使命

AAS-SIMP（数据库安全综合管理平台）是企业级数据库安全态势感知与审计平台。系统从多个数据库安全设备（防火墙、动态脱敏设备等）采集审计日志，经过 Kafka 消息总线处理，存储于 Elasticsearch 和 MySQL，提供全面的安全运营 Web 控制台，包括风险态势分析、审计日志检索、敏感数据发现、风险评估预测和报表统计等功能。

## 全局强制阅读顺序

1. `CLAUDE.md` — 本文档，了解项目全貌和联邦结构
2. `NON_FUNCTIONAL.md` — 全局硬约束，开发前必须遵守
3. `SYSTEM_ARCHITECTURE.md` — 系统架构，理解模块分解与协作方式
4. `GLOBAL_CONTRACTS.md` — 跨模块通信规范，理解模块间边界
5. `SHARED_DOMAIN.md` — 通用语言，统一领域概念认知
6. 进入具体模块目录阅读其 `CLAUDE.md`

## 联邦模块清单

| 模块 | 路径 | 角色 |
|------|------|------|
| `common-module` | `common-module/` | 公共库（Zookeeper、Redis、SM4、数据库备份等），仅作为依赖提供，不独立部署 |
| `socket-comm` | `socket-comm/` | 进程间 TCP Socket 通信库，仅作为依赖提供，不独立部署 |
| `collection-engine` | `collection-engine/` | 采集引擎：UDP Syslog 接收、数据分类、Kafka 生产 |
| `kafka-client` | `kafka-client/` | Kafka 消费引擎：策略解析、Elasticsearch/MySQL 存储 |
| `manager-center` | `manager-center/` | 管理中心：进程间 Socket 消息路由转发枢纽 |
| `report-engine` | `report-engine/` | 报表引擎：定时统计、预聚合、报表数据存储 |
| `web-app` | `web-app/` | Web 应用：REST API、WebSocket、用户认证、前端服务 |
| `daemon` | `daemon/` | 守护进程：Zookeeper 服务监控、异常自动恢复 |

## 全局冲突解决优先级

1. `NON_FUNCTIONAL.md` 中的禁止项 > 任何模块内部实现决策
2. `GLOBAL_CONTRACTS.md` 中的契约 > 模块 `EXPORTS.md` 中的接口
3. 模块 `EXPORTS.md` > 模块 `INTERNAL_LOGIC.md`
4. 根目录文档 > 模块目录文档
5. 代码 > 文档（当文档与代码不一致时，以代码为准并及时更新文档）

## 文档图完整性

从本文档出发，可通过以下路径追踪到所有文档：
- `docs/CLAUDE.md` → `docs/SYSTEM_ARCHITECTURE.md` → 各模块 `docs/[module]/CLAUDE.md`
- `docs/CLAUDE.md` → `docs/GLOBAL_CONTRACTS.md` → 各模块 `docs/[module]/EXPORTS.md`
- `docs/CLAUDE.md` → `docs/SHARED_DOMAIN.md` → 各模块 `docs/[module]/INTERNAL_LOGIC.md`
- 各模块 `docs/[module]/DEPENDENCIES.md` → 引用其他模块 `EXPORTS.md`
