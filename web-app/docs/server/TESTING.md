# TESTING.md — server 服务器配置模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | 发送工具类 |
| 集成测试 | Spring Boot Test | Controller |

## Mock 边界

- 邮件发送 → Mock JavaMailSender
- 短信发送 → Mock HTTP Client
- SNMP/Syslog → Mock Socket
- FTP/SFTP → Mock Camel
