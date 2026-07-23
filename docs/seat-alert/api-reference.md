# 告警规则 & 黑白名单 — 接口文档

`Base URL`: 无统一前缀，各 Controller 自带 `@RequestMapping`

`共通响应体 (WebResult)`:

```json
{
  "code": "000000000",       // 成功
  "message": "操作成功",
  "data": null               // 具体数据见各接口
}
```

错误时 `code = "999999999"`, `message` 为具体错误描述。

---

## 告警规则 `/alert-rule`

### 分页查询

`GET /alert-rule`

**请求参数 (Query)**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| pageNo | Integer | 否 | 1 | 页码 |
| pageSize | Integer | 否 | 10 | 每页条数 |
| keyword | String | 否 | — | 规则名称模糊搜索 |
| logType | Byte | 否 | — | 日志类型 0-平台登录 1-IM登录 2-DLP |
| ruleType | Byte | 否 | — | 规则类型 0-普通 1-组合 |
| status | Byte | 否 | — | 状态 0-禁用 1-启用 |

**成功响应 data**

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "total": 42,
  "list": [
    {
      "id": 1,
      "name": "规则名称",
      "description": "描述",
      "logType": 0,
      "ruleType": 0,
      "level": 1,
      "levelName": "一级（红）",
      "linkType": 0,
      "conditions": "[{\"field\":\"operatorName\",\"operator\":\"等于\",\"value\":\"张三\"}]",
      "combineDuration": null,
      "combineCount": null,
      "combineRuleId": null,
      "whitelistId": null,
      "blacklistId": 1,
      "status": 1,
      "createTime": 1700000000000,
      "updateTime": 1700000000000
    }
  ]
}
```

---

### 详情

`GET /alert-rule/detail?id={id}`

**请求参数 (Query)**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 规则 ID |

**成功响应 data** — 同列表 `AlertRule` 对象（不含 `levelName` 等计算字段时前端自行计算）。

---

### 新增

`POST /alert-rule`

**请求体 (JSON)**

```json
{
  "name": "规则名称",
  "description": "描述",
  "logType": 0,
  "ruleType": 0,
  "level": 1,
  "linkType": 0,
  "conditions": "[{\"field\":\"登录时间\",\"operator\":\"等于\",\"value\":\"2024-01-01 12:00:00\"}]",
  "combineDuration": null,
  "combineCount": null,
  "combineRuleId": null,
  "whitelistId": null,
  "blacklistId": 1,
  "status": 1
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 规则名称 |
| description | String | 否 | 描述 |
| logType | Byte | 是 | 日志类型: 0-平台登录 1-IM登录 2-DLP |
| ruleType | Byte | 是 | 规则类型: 0-普通 1-组合 |
| level | Byte | 是 | 告警等级: 1-一级(红) 2-二级(橙) 3-三级(黄) |
| linkType | Byte | 普通规则必填 | 关联方式: 0-与 1-或 |
| conditions | String (JSON) | 普通规则必填 | 匹配条件 JSON 数组，见下方格式 |
| combineDuration | Integer | 组合规则时 | 统计频率(分钟) |
| combineCount | Integer | 组合规则时 | 触发次数 |
| combineRuleId | Integer | 组合规则时 | 引用的普通规则 ID |
| whitelistId | Integer | 否 | 白名单 ID（必须为启用且未删除状态） |
| blacklistId | Integer | 否 | 黑名单 ID（必须为启用且未删除状态） |
| status | Byte | 否 | 默认 1-启用 |

**conditions 格式**

```json
[
  {
    "field": "operatorName",
    "operator": "equals",
    "value": "张三"
  },
  {
    "field": "loginTime",
    "operator": "gte",
    "value": "800"
  },
  {
    "field": "ipAddress",
    "operator": "equals",
    "value": "192.168.1.1"
  }
]
```

| field 可选值 | 说明 | operator 可选值 |
|-------------|------|----------------|
| operatorName | 用户名称 | equals, notEquals, contains, notContains |
| operatorAccount | 用户账号 | equals, notEquals, contains, notContains |
| ipAddress | 席位IP | equals, notEquals, contains, notContains |
| loginTime | 登录时间（数字字符串，如"800"=8:00, "1900"=19:00） | gte, lte |
| sendMethod | 发送方式（单发/群发） | equals, notEquals, contains, notContains |
| fileName | 文件名 | equals, notEquals, contains, notContains |
| senderParty | 参与方 | crossCamp |
| logType | 操作类型（登录/登出/私聊/群聊） | equals, notEquals, contains, notContains |
| operationResult | 匹配结果（成功/失败） | equals, notEquals, contains, notContains |

字段名直接使用 `AlertRawLog` Java 属性名，引擎通过反射从实体取值，未匹配时查 `extensions` Map。

**operator 枚举值与中文对照**

| 枚举值 | 中文标签 | 说明 |
|--------|---------|------|
| equals | 等于 | 精确匹配 |
| notEquals | 不等于 | 反向精确匹配 |
| gte | 大于等于 | 数值比较 >= |
| lte | 小于等于 | 数值比较 <= |
| lt | 小于 | 数值比较 < |
| gt | 大于 | 数值比较 > |
| contains | 包含 | 模糊子串匹配 |
| notContains | 不包含 | 反向模糊匹配 |
| crossCamp | 跨阵营 | 跨阵营通讯检测 |

**成功响应**

```json
{
  "code": "000000000",
  "message": "创建成功",
  "data": 1     // 新规则 ID
}
```

---

### 更新

`PUT /alert-rule`

**请求体 (JSON)** — 同新增，额外需传入 `id`

**成功响应**

```json
{
  "code": "000000000",
  "message": "更新成功",
  "data": 1
}
```

---

### 删除

`POST /alert-rule/delete`

**请求体 (JSON)** — Integer 数组，支持单条或批量

```json
[1, 2, 3]
```

**成功响应**

```json
{
  "code": "000000000",
  "message": "成功删除3条",
  "data": 3
}
```

---

### 切换状态

`PUT /alert-rule/status`

**请求体 (JSON)**

```json
{
  "id": 1
}
```

**成功响应**

```json
{
  "code": "000000000",
  "message": "状态已更新",
  "data": null
}
```

---

## 黑白名单 `/alert-access-list`

### 分页查询

`GET /alert-access-list`

**请求参数 (Query)**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| pageNo | Integer | 否 | 1 | 页码 |
| pageSize | Integer | 否 | 10 | 每页条数 |
| keyword | String | 否 | — | 名称模糊搜索 |
| type | Byte | 否 | — | 类型: 0-白名单 1-黑名单 |

**成功响应 data**

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "total": 10,
  "list": [
    {
      "id": 1,
      "name": "白名单-研发部",
      "type": 0,
      "status": 1,
      "remark": "研发部员工",
      "userCount": 5,
      "createTime": 1700000000000,
      "updateTime": 1700000000000
    }
  ]
}
```

---

### 详情

`GET /alert-access-list/detail?id={id}`

**成功响应 data**

```json
{
  "id": 1,
  "name": "白名单-研发部",
  "type": 0,
  "status": 1,
  "remark": "研发部员工",
  "userCount": 5,
  "users": [
    { "id": 1, "listId": 1, "userName": "张三", "userAccount": "zhangsan" },
    { "id": 2, "listId": 1, "userName": "李四", "userAccount": "lisi" }
  ],
  "createTime": 1700000000000,
  "updateTime": 1700000000000
}
```

---

### 获取启用列表 (下拉用)

`GET /alert-access-list/names?type={type}`

返回 `status=1 AND is_delete=0` 的列表，仅含基本字段。

**请求参数 (Query)**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| type | Byte | 否 | 0-白名单 1-黑名单，不传则返回全部 |

**成功响应 data**

```json
[
  { "id": 1, "name": "白名单-研发部", "type": 0, "status": 1 },
  { "id": 2, "name": "白名单-市场部", "type": 0, "status": 1 }
]
```

---

### 新增

`POST /alert-access-list`

**请求体 (JSON)**

```json
{
  "name": "白名单-研发部",
  "type": 0,
  "status": 1,
  "remark": "研发部员工",
  "users": [
    { "userName": "张三", "userAccount": "zhangsan" },
    { "userName": "李四", "userAccount": "lisi" }
  ]
}
```

**成功响应**

```json
{
  "code": "000000000",
  "message": "创建成功",
  "data": 1
}
```

---

### 更新

`PUT /alert-access-list`

**请求体 (JSON)** — 同新增，额外需传入 `id`

---

### 删除

`POST /alert-access-list/delete`

**请求体 (JSON)** — Integer 数组

```json
[1, 2]
```

---

**请求体 (JSON)**

```json
{
  "id": 1
}
```

---

## 告警事件 `/alert-result`

### 分页查询

`GET /alert-result`

**请求参数 (Query)**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| pageNo | Integer | 否 | 1 | 页码 |
| pageSize | Integer | 否 | 10 | 每页条数 |
| keyword | String | 否 | — | 规则名称模糊搜索 |
| alertLevel | Byte | 否 | — | 告警等级 1-红 2-橙 3-黄 |
| ruleType | Byte | 否 | — | 规则类型 0-普通 1-组合 |

**成功响应 data**

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "total": 42,
  "list": [
    {
      "id": 1,
      "alertRuleId": 1,
      "ruleType": 0,
      "normalRuleId": null,
      "alertLevel": 2,
      "triggerCount": 1,
      "createTime": 1700000000000,
      "auditDetails": null
    }
  ]
}
```

---

### 详情

`GET /alert-result/detail?id={id}`

---

### 删除（批量）

`POST /alert-result/delete`

```json
[1, 2, 3]
```

---

## 告警规则匹配引擎 (内部服务，无REST接口)

**调用方式**：Spring Bean 注入 `AlertMatchService`，调用 `match(AlertRawLog rawLog)` 方法。

**输入**：`AlertRawLog` 实体，必填字段：
- `logSource` — 日志来源（PLATFORM_LOGIN / IM / DLP）
- `recordId` — 原始日志全局ID
- `operatorName` / `operatorAccount` — 操作人信息
- `ipAddress` — 席位IP
- `logTime` — 时间戳（毫秒）
- 其他可选字段及 `extensions` Map

**输出**：命中的规则数量（int），告警事件已写入 `alert_result` 表。
