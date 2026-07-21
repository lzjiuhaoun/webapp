# CLAUDE.md — server 服务器配置模块

## 模块使命

管理系统通知渠道和协议配置：钉钉、邮件、短信、SNMP、Syslog、FTP/SFTP和WebSocket，为系统告警和通知提供底层发送能力。

## 技术栈

- MyBatis + MySQL
- Spring Mail
- Apache Camel FTP
- SNMP4J
- syslog4j

## 文档索引

| 文档 | 路径 |
|------|------|
| 出口契约 | `EXPORTS.md` |
| 内部逻辑 | `INTERNAL_LOGIC.md` |
| 依赖声明 | `DEPENDENCIES.md` |
| 测试策略 | `TESTING.md` |
| 任务书 | `TASKS.md` |
| 验收标准 | `ACCEPTANCE.md` |

## 代码结构

```
com.ankki.webapp/
├── controller/server/
│   ├── DingTalkConfigController      ← 钉钉配置
│   ├── FtpConfigController           ← FTP配置
│   ├── MailConfigController          ← 邮件配置
│   ├── MsgPlatformController         ← 消息平台
│   ├── SftpConfigController          ← SFTP配置
│   ├── SmsConfigController           ← 短信配置
│   ├── SnmpConfigController          ← SNMP配置
│   ├── SyslogConfigController        ← /internet-server (Syslog)
│   └── WebsocketConfigController     ← /internet-server (WebSocket)
├── service/
│   ├── DingTalkConfigService/Impl
│   ├── FtpConfigService/Impl
│   ├── MailConfigService/Impl
│   ├── MsgPlatformService/Impl
│   ├── SftpConfigService/Impl
│   ├── SmsConfigService/Impl
│   ├── SnmpConfigService/Impl
│   ├── SyslogConfigService/Impl
│   ├── WebsocketConfigService/Impl
│   └── ScreenMonitorService/Impl
├── dao/server/
│   ├── DingTalkConfigMapper, FtpConfigMapper, MailConfigMapper
│   ├── MsgPlatformConfigMapper, SftpConfigMapper, SmsConfigMapper
│   ├── SnmpConfigMapper, SyslogConfigMapper
│   └── WebsocketConfigMapper
└── model/server/
    ├── DingTalkConfig, FtpConfig, MailConfig
    ├── MsgPlatformConfig, SftpConfig, SmsConfig
    ├── SnmpConfig, SyslogConfig, WebsocketConfig
    └── MailBean
```

## 发送工具类

```
com.ankki.webapp.util/send/
├── MailUtils.java          ← 邮件发送
├── SendSnmpUtil.java       ← SNMP Trap发送
├── SendSyslogUtil.java     ← Syslog发送
└── SmsUtils.java           ← 短信发送
```
