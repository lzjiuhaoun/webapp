# AGENTS.md — 看板B02

## 项目速览

AAS-SIMP（数据安全综合治理平台/看板B02）—— 企业级数据库安全态势感知与审计平台。多进程联邦架构，Spring Boot 2.5.15 / JDK 1.8，单模块 Maven 项目。

## 关键文档（必读顺序）

1. `NON_FUNCTIONAL.md` — 全局硬约束（禁止项、强制项），改代码前先过一遍
2. `SYSTEM_ARCHITECTURE.md` — 系统架构、模块角色、数据流
3. `GLOBAL_CONTRACTS.md` — 跨模块通信契约（Socket 协议格式、端口、Topic）
4. `SHARED_DOMAIN.md` — 领域术语统一
5. 进入模块目录读 `docs/CLAUDE.md`
6. `docs/application-run.md` — 启动/运行指南

## 模块现状

| 模块 | 状态 | 说明 |
|------|------|------|
| `web-app` | **有代码** | WAR 包，REST API + Vue 2 前端，独立 JVM 进程 |
| `collection-engine` | 仅文档 | 无 pom.xml 无 Java 源 |
| `kafka-client` | 仅文档 | 同上 |
| `manager-center` | 仅文档 | 同上 |
| `report-engine` | 仅文档 | 同上 |
| `daemon` | 仅文档 | 同上 |
| `common-module` | 仅文档 | 共享库，无代码 |
| `socket-comm` | 仅文档 | Socket 通信库，无代码 |

根 pom.xml 仅声明了 `web-app` 一个子模块。其余模块的代码尚未实现。

## 开发命令

```bash
# 后端启动（必须 -pl web-app，不能加 -am）
mvn clean spring-boot:run -pl web-app

# WAR 打包
mvn clean package -pl web-app -DskipTests

# 前端
cd web-app/frontend
npm install
npm run dev        # http://localhost:8081，代理到后端 8543
npm run build      # 输出到 dist/
```

## 必须知道的事

### 后端
- **DM8 JDBC 驱动**：`web-app/lib/DmJdbcDriver18.jar` 需手动从 DM8 安装目录复制，不在 Maven 仓库中
- **DM8 SQL 限制**：不支持 `DEFAULT NOW()`（用 BIGINT + Java 层赋值）、不支持 `DROP TABLE IF EXISTS`、使用大写标识符
- **Mapper 双份**：MyBatis XML 必须同时提供 `mapper/`（MySQL）和 `dm/mapper/`（DM8）两套

### 前端
- **core-js 兼容问题**：`core-js@2` 在顶层，Vue CLI 5 需要 `core-js@3` → `npm install core-js@^3.49.0`
- **Dev server 超时**：另开终端跑 `npm run dev`，不要与 Maven 共用同一个终端

### 交付约束
- 所有 REST Controller 必须继承 `BaseController`（自带 `@CrossOrigin`）
- 密码存储用 SM4，API 通信用 SM2/RSA 非对称加密
- 统一北京时间 UTC+8
- 无需测试（项目无测试代码），只需编译通过
- **Java 命名规范**：类名、方法名必须见名知意，禁止过于简略的缩写（如 `add` / `get` / `set` 等常规命名不受限，但业务方法必须表达完整语义）
- **Java 注释规范**：所有 Java 类、方法、字段必须添加必要的 Javadoc 注释，符合《阿里巴巴 Java 开发手册》要求（类注释含 `@author`，方法注释含 `@param`/`@return`，字段注释说明业务含义）
- **Java 日志规范**：所有 Java 类必须使用 Lombok `@Slf4j` 注解声明日志；关键业务操作（增删改查、状态切换）必须记录 info 日志；参数校验失败、业务规则阻断必须记录 warn 日志；异常捕获处必须记录 error 日志（含异常堆栈）

### Git
- 远程操作必须走本地代理：`git config http.proxy http://127.0.0.1:7897`

## 全局冲突优先级

1. `NON_FUNCTIONAL.md` 禁止项 > 任何模块内部决策
2. `GLOBAL_CONTRACTS.md` 契约 > 模块 `EXPORTS.md` 接口
3. 代码 > 文档（不一致时以代码为准并及时更新文档）
