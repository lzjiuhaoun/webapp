# TESTING.md — auth 认证模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | Service 层方法 |
| 集成测试 | Spring Boot Test | Controller 端点 |

## Mock 边界

- CAS 服务器 → Mock CAS 响应
- 外部 IP 白名单 → Mock LoginIpConfigService

## 测试目录

```
src/test/java/com/ankki/webapp/
└── (待补充 auth 相关测试)
```
