# EXPORTS.md — auth 认证模块

**版本：** v1.0.0

## 提供的接口

### REST API

| 端点 | 方法 | 描述 | 参数 | 返回值 |
|------|------|------|------|--------|
| `/login` | POST | 用户登录 | 用户名、密码、验证码 | AjaxResult |
| `/login/cas-nmxy-login` | GET | CAS单点登录 | CAS回调参数 | 重定向 |
| `/user` | GET/POST/PUT/DELETE | 用户管理CRUD | 用户对象 | AjaxResult |

### Service 接口

| 接口 | 方法 | 描述 | 调用方 |
|------|------|------|--------|
| `LoginService` | `login()` | 用户登录验证 | 全模块 |
| `LoginService` | `logout()` | 用户登出 | 全模块 |
| `UserService` | `getUserInfo()` | 获取当前用户信息 | 全模块 |
| `UserService` | `checkPermission()` | 校验用户权限 | 全模块 |
| `PermissionControlService` | `getPermissionList()` | 获取权限列表 | system, config |

### 拦截器

| 拦截器 | 拦截路径 | 功能 |
|--------|---------|------|
| `AuthorizationInterceptor` | `/**` | 登录状态校验、权限校验 |

## 数据模型

| 实体 | 说明 |
|------|------|
| `User` | 用户基本信息 |
| `UserInfo` | 用户扩展信息 |
| `LoginInfo` | 登录会话信息 |
| `Module` | 功能模块 |
| `ModuleGroup` | 模块分组 |
| `UserPopedom` | 用户权限 |
