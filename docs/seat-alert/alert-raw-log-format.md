# AlertRawLog 字段说明 — 规则匹配输入标准

## 概述

`AlertRawLog` 是告警规则引擎的标准化输入实体。各日志源（统一平台登录日志、IM 即时通讯日志等）的原始日志经过解析层转换后，统一填充为该实体，逐条送入规则引擎进行匹配。

## 字段清单

| # | 字段 | 类型 | 必需 | 说明 | 匹配条件中对应的字段名 | 示例值 |
|---|------|------|------|------|------------------------|--------|
| 1 | `logSource` | String | **是** | 日志来源，用于规则引擎筛选目标规则 | —（引擎内部按此字段路由到对应规则的 `logSource`） | `"IM"` |
| 2 | `operatorName` | String | **是** | 操作人名称 | 用户名称 | `"张三"` |
| 3 | `operatorAccount` | String | **是** | 操作人账号 | 用户账号 | `"zhangsan"` |
| 4 | `ipAddress` | String | **是** | 席位IP | 席位IP | `"192.168.19.189"` |
| 5 | `logTime` | Long | **是** | 日志时间戳（毫秒） | 登录时间 | `1784168909684` |
| 6 | `logType` | String | **是** | 操作类型 | —（条件中通过 `logType` 字段匹配） | `"私聊"` |
| 7 | `operationResult` | String | **是** | 操作结果 | — | `"成功"` |
| 8 | `sendMethod` | String | 否 | 发送方式，由解析层从 `logType` 派生：私聊→单发，群聊→群发 | 发送方式 | `"单发"` |
| 9 | `fileName` | String | 否 | 文件名称（文件传输相关日志） | 文件名 | `"【公开】第四季度_许飞.doc"` |
| 10 | `senderParty` | String | 否 | 发送方参与方/阵营 | 参与方 | `"红方"` |
| 11 | `receivers` | List\<Receiver\> | 否 | 接收人列表（含阵营），用于跨阵营判断和未来接收方黑白名单 | —（跨阵营运算符内部使用） | 见下方示例 |
| 12 | `description` | String | 否 | 日志描述原文 | — | `"张三 (zhangsan) 给刘某 (liumou)发送了一条公开文件消息"` |
| 13 | `rawData` | String | 否 | 原始日志数据（JSON），保留完整原始记录用于追溯 | — | 见下方示例 |
| 14 | `extensions` | Map\<String,Object\> | 否 | 扩展字段，未来新增匹配字段无需改实体 | 动态 | `{"browser": "Chrome 142"}` |

### Receiver 内部类字段

| 字段 | 类型 | 必需 | 说明 | 示例值 |
|------|------|------|------|--------|
| `userName` | String | **是** | 接收人账号 | `"wangwu"` |
| `name` | String | 否 | 接收人姓名 | `"王五"` |
| `party` | String | **是** | 接收人参与方/阵营 | `"红方"` |

## 必需字段判定规则

- **跨日志来源通用必需**：`logSource`、`operatorName`、`operatorAccount`、`ipAddress`、`logTime`、`logType`、`operationResult` — 所有日志源都必须填充
- **IM 聊天日志补充必需**：`sendMethod`、`senderParty`、`receivers` — IM 聊天/文件传输场景必须填充
- **文件传输场景补充必需**：`fileName` — IM 发送文件时必须填充

## 各日志源映射参考

### IM 即时通讯日志 → AlertRawLog

| AlertRawLog 字段 | IM 原始日志字段 | 说明 |
|-------------------|----------------|------|
| `logSource` | — | 固定为 `"IM"` |
| `operatorName` | `operationLog.operator` | |
| `operatorAccount` | `operationLog.operatorUserName` | |
| `ipAddress` | `operationLog.ip` | |
| `logTime` | `operationLog.createTime` | |
| `logType` | `operationLog.logType` | 登录/登出/私聊/群聊 |
| `operationResult` | `operationLog.operationResult` | |
| `sendMethod` | — | 由 `logType` 派生：私聊→单发，群聊→群发 |
| `fileName` | `operationLog.fileName` | 日志类型为上传文件时有值 |
| `senderParty` | `operationLog.participatyParty` | |
| `receivers` | `receiveUserInfo[]` | 数组映射，每个元素取 `userName`、`name`、`participatyParty` |
| `description` | `operationLog.describe` | |
| `rawData` | 完整 IM 日志 JSON | 原样保留 |

### 统一平台登录日志 → AlertRawLog

| AlertRawLog 字段 | 平台日志字段 | 说明 |
|-------------------|-------------|------|
| `logSource` | — | 固定为 `"PLATFORM_LOGIN"` |
| `operatorName` | `nickname` | |
| `operatorAccount` | `username` | |
| `ipAddress` | `ipAddress` | |
| `logTime` | `createTime` | 需将 `"2026-07-15 11:40:58"` 格式转为毫秒时间戳 |
| `logType` | `loginType` | 0→登录, 1→登出 |
| `operationResult` | `status` | 0→成功, 1→失败 |
| `sendMethod` | — | 平台登录日志无此概念，填 `null` |
| `senderParty` | — | 平台登录日志无此概念，填 `null` |
| `receivers` | — | 平台登录日志无此概念，填 `null` |
| `description` | `errorMsg` | 登录失败时有值 |
| `rawData` | 完整平台登录日志 JSON | 原样保留 |

## 完整 JSON 示例

```json
{
  "logSource": "IM",
  "operatorName": "张三",
  "operatorAccount": "zhangsan",
  "ipAddress": "192.168.19.189",
  "logTime": 1784168909684,
  "logType": "私聊",
  "operationResult": "成功",
  "sendMethod": "单发",
  "fileName": "【公开】第四季度_许飞.doc",
  "senderParty": "红方",
  "receivers": [
    {
      "userName": "wangwu",
      "name": "王五",
      "party": "红方"
    },
    {
      "userName": "lisi",
      "name": "李四",
      "party": "红方"
    }
  ],
  "description": "张三 (zhangsan) 给刘某 (liumou)发送了一条公开文件消息",
  "rawData": "{ 原始 IM 日志的完整 JSON }",
  "extensions": {}
}
```
