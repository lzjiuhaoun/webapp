# CLAUDE.md — auth 认证模块

## 模块使命

管理用户认证、登录、权限控制和会话管理，为系统其他模块提供统一身份认证能力。

## 技术栈

- Spring Security（CAS 集成）
- CAS Client（com.ankki.cas + com.cnkki.cas）
- HttpSession / Token
- MyBatis + MySQL

## 文档索引

| 文档 | 路径 | 说明 |
|------|------|------|
| 出口契约 | `EXPORTS.md` | 对外提供的认证接口 |
| 内部逻辑 | `INTERNAL_LOGIC.md` | 认证流程和权限控制内幕 |
| 依赖声明 | `DEPENDENCIES.md` | 模块外部依赖 |
| 测试策略 | `TESTING.md` | 测试方案 |
| 任务书 | `TASKS.md` | 开发任务 |
| 验收标准 | `ACCEPTANCE.md` | 模块验收 |

## 代码结构

```
com.ankki.webapp/
├── controller/user/
│   ├── LoginController.java       ← /login
│   └── UserController.java        ← /user
├── service/
│   ├── LoginService/Impl          ← 登录逻辑
│   ├── UserService/Impl           ← 用户管理
│   └── RandomValidateCodeService  ← 验证码
├── dao/user/
│   ├── UserMapper.java
│   ├── ModuleMapper.java
│   ├── ModuleGroupMapper.java
│   └── UserPopedomMapper.java
├── model/user/
│   ├── User.java
│   ├── UserInfo.java
│   ├── LoginInfo.java
│   ├── Module.java
│   ├── ModuleGroup.java
│   ├── UserPopedom.java
│   ├── Randcode.java
│   └── TableTime.java
└── filter/
    ├── AuthorizationInterceptor.java ← 权限拦截
    └── InterceptorConfig.java        ← 拦截器配置
```

## Source of Truth

- 用户数据源：MySQL `t_user` 表
- 权限数据源：MySQL `t_module`, `t_module_group`, `t_user_popedom` 表
- CAS 配置：`resources/casFilterConfig.xml`
