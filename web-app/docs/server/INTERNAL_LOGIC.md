# INTERNAL_LOGIC.md — server 服务器配置模块

## 核心流程

### 告警通知发送
```
system 模块触发告警 → 查找通知渠道配置(server模块)
                        ↓
              根据渠道类型选择发送工具
              ├── MailUtils → JavaMail
              ├── SmsUtils → 短信网关HTTP
              ├── SendSnmpUtil → SNMP4J Trap
              ├── SendSyslogUtil → syslog4j UDP/TCP
              └── DingTalk HTTP → Webhook
                        ↓
              记录发送结果
```

### FTP/SFTP 文件传输
- 使用 Apache Camel FTP 组件
- 支持主动/被动模式
- SFTP 使用 SSH 密钥认证

### 邮件模板
- 模板文件：`resources/templates/emailTemplate.html`
- 使用 Thymeleaf 渲染
- 支持HTML格式
