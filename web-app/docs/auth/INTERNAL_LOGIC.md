# INTERNAL_LOGIC.md — auth 认证模块

## 认证流程

```
前端请求 → InterceptorConfig → AuthorizationInterceptor
                                    ↓
                              是否登录？
                              ↓否        ↓是
                            /login    校验权限
                                    ↓        ↓
                              LoginService   放行
                                    ↓
                              CAS认证 / 本地认证
                                    ↓
                              创建Session / Token
                                    ↓
                              返回用户信息
```

## 关键实现

### CAS 单点登录
- CAS Filter 配置在 `resources/casFilterConfig.xml`
- 使用 `com.ankki.cas` 和 `com.cnkki.cas` 两个 CAS 客户端
- 登录成功后通过回调创建 Session

### 权限拦截
- `AuthorizationInterceptor` 实现 `HandlerInterceptor`
- 在 `InterceptorConfig` 中注册，拦截 `/**`
- 跳过路径：`/login`, `/captcha`, Swagger 相关路径等
- 权限校验基于用户角色和模块权限

### 验证码
- `RandomValidateCodeService` 生成图形验证码
- 验证码存储在 Session 中，登录时校验

## 安全机制

- IP白名单限制（通过 system 模块的 LoginIpConfig）
- 密码加密存储
- Session 超时管理
- 登录失败次数限制
