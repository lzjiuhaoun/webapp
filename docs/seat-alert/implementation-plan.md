# 告警规则配置 - 实现计划

## 领域模型

### 席位状态 (Seat Status)
- **离线** / **在线** — 在线状态下才有告警状态
- **三级（黄色）** / **二级（橙色）** / **一级（红色）**

### 告警规则 (Alert Rule)
定义何时对席位产生告警的判定单元。

| 分类 | 说明 |
|------|------|
| **普通规则** | 含匹配条件集合 + 告警等级 + 黑白名单引用 |
| **组合规则** | 引用一条普通规则，配置触发次数阈值，次数对应等级。不同等级建多条规则 |

### 匹配条件 (Matching Condition)
三元组：**字段 + 运算符 + 值**，多行条件统一关联方式（全 AND 或全 OR）。

字段不固定，按日志源动态自配。运算符包含：等于、不等于、大于等于、小于、包含、跨阵营。

**跨阵营**：值格式如 `红方|蓝方、白方|蓝方、绿方|红方|白方`，`|` 同阵营，`、` 分隔阵营组。发送方与接收方属同一阵营组不告警，否则告警。

### 匹配条件中的运算符定义

| 运算符 | 说明 | 适用场景 |
|--------|------|----------|
| 等于 | 字段值精确匹配 | 用户名称、用户账号等精确字段 |
| 不等于 | 字段值取反匹配 | 排除特定值 |
| 大于等于 | 数值或时间比较 | 登录时间 >= 08:00 |
| 小于等于 | 数值或时间比较 | 登录时间 <= 19:00 |
| 包含 | 模糊子串匹配 | 文件名包含"机密"等敏感词 |
| 跨阵营 | 参与方跨阵营检测 | 发送方与接收方不在同一阵营组 |

### 黑白名单处理流程 (优先级: 黑名单 > 白名单)

```
用户是否命中匹配条件？
 ├── 否 → 不告警
 └── 是 → 是否配置了黑名单？
           ├── 是 → 用户是否在黑名单中？
           │         ├── 是 → 告警！（白名单无效）
           │         └── 否 → 不告警
           └── 否 → 是否配置了白名单？
                     ├── 是 → 用户在白名单中？→ 是→豁免不告警 / 否→告警
                     └── 否 → 告警
```

### 时间段配置

- 一个规则只对应一种时间段场景（工作/非工作），不同场景建不同规则
- **工作时间示例**：条件1=`登录时间 >= 08:00`，条件2=`登录时间 <= 19:00`，关联方式=与
- **非工作时间示例**：配置 `登录时间 < 08:00 或 > 19:00`，通过多规则或取反实现

---

## 数据库表

### `alert_rule`

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT(PK) | 主键自增 |
| name | VARCHAR(64) | 规则名称 |
| description | VARCHAR(255) | 描述 |
| log_type | TINYINT | 0=平台登录日志, 1=IM登录日志, 2=DLP日志 |
| rule_type | TINYINT | 0=普通规则, 1=组合规则 |
| level | TINYINT | 告警等级: 1=一级红, 2=二级橙, 3=三级黄 |
| link_type | TINYINT | 0=与, 1=或 |
| conditions | TEXT | JSON 字符串: `[{"field":"文件名","operator":"包含","value":"机密"},...]` |
| combine_duration | INT | 组合规则统计频率(分钟) |
| combine_count | INT | 组合规则触发次数阈值 |
| combine_rule_id | INT | 组合规则引用的普通规则 ID |
| whitelist_id | INT | FK → alert_access_list.id |
| blacklist_id | INT | FK → alert_access_list.id |
| status | TINYINT | 0=禁用, 1=启用 |
| create_time | BIGINT | 创建时间戳 |
| update_time | BIGINT | 更新时间戳 |
| is_delete | TINYINT | 0=未删, 1=已删 |

---

## REST API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/alert-rule` | 分页列表（keyword, logType, ruleType, status 筛选） |
| GET | `/alert-rule/detail?id=` | 详情 |
| POST | `/alert-rule` | 新增（conditions 整体 JSON 提交） |
| PUT | `/alert-rule` | 编辑 |
| POST | `/alert-rule/delete` | 批量删除 |
| PUT | `/alert-rule/status` | 切换启用/禁用 |

---

## 后端文件清单

```
web-app/src/main/java/com/ankki/webapp/
├── model/config/AlertRule.java
├── dao/config/AlertRuleMapper.java
├── service/AlertRuleService.java
├── service/impl/AlertRuleServiceImpl.java
└── controller/config/AlertRuleController.java

web-app/src/main/resources/
├── mapper/AlertRuleMapper.xml          (MySQL)
└── dm/mapper/AlertRuleMapper.xml       (DM8)

sql/init-alert-rule.sql
```

### 条件 JSON 序列化处理

AlertRule 实体类中 `conditions` 字段用 `String` 类型存储，Service 层通过 Jackson `ObjectMapper` 在 `List<Map<String,String>>` 与 JSON 字符串之间转换。

```java
private String conditions;

// Service 层写入时
String json = objectMapper.writeValueAsString(conditionList);

// Service 层读取时
List<Map<String,String>> list = objectMapper.readValue(
    record.getConditions(), 
    new TypeReference<List<Map<String,String>>>(){}
);
```

---

## 前端文件清单

```
web-app/frontend/src/
├── api/alertRule.js                    # API 封装
├── views/alert/AlertRuleConfig.vue     # 页面组件
└── router/index.js                     # + 路由 /alert/rule
```

### 页面组成

| 区域 | 说明 |
|------|------|
| 规则表格列表 | 名称、日志类型、规则类型、告警等级、黑白名单、状态、更新时间、操作 |
| 新增/编辑弹窗 | 基本信息 → 规则类型 → 匹配条件（动态行列增删）→ 组合规则配置（引用普通规则+触发次数）→ 黑白名单引用 |
| 详情弹窗 | 只读展示全部字段 |
| 启用/禁用开关 | el-switch + 确认弹窗 |

### 弹窗内容

**基本信息区**：规则名称、日志类型（下拉）、描述

**规则类型区**：普通规则/组合规则 单选

- 普通规则：告警等级（一级/二级/三级）、匹配条件列表（字段下拉+运算符下拉+值输入，动态增删行）
- 组合规则：引用普通规则下拉、触发次数输入

**匹配条件区**（仅普通规则）：条件行（字段+运算符+值），关联方式（与/或）

**黑白名单区**：白名单下拉、黑名单下拉（引用已启用的 AccessList）

---

## 编码约束

- Controller 继承 `BaseController`
- 响应使用 `WebResult` 包装
- 时间戳用 `System.currentTimeMillis()`，DM8 存 BIGINT
- 软删除用 `is_delete` 字段
- 双数据库 Mapper XML（MySQL `/mapper/` + DM8 `/dm/mapper/`）
- DM8 SQL 大写标识符 + `SCM_RJ_SIMP.` 前缀
- 前端 Vue 2 + Element UI，使用选项式 API
