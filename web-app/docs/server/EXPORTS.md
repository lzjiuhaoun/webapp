# EXPORTS.md — server 服务器配置模块

**版本：** v1.0.0

## REST API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/server/dingtalk` | GET/POST/PUT/DELETE | 钉钉配置 |
| `/server/ftp` | GET/POST/PUT/DELETE | FTP配置 |
| `/server/mail` | GET/POST/PUT/DELETE | 邮件配置 |
| `/server/msg-platform` | GET/POST/PUT/DELETE | 消息平台配置 |
| `/server/sftp` | GET/POST/PUT/DELETE | SFTP配置 |
| `/server/sms` | GET/POST/PUT/DELETE | 短信配置 |
| `/server/snmp` | GET/POST/PUT/DELETE | SNMP配置 |
| `/internet-server/syslog` | GET/POST/PUT/DELETE | Syslog配置 |
| `/internet-server/websocket` | GET/POST/PUT/DELETE | WebSocket配置 |

## Service 接口

| 接口 | 描述 | 调用方 |
|------|------|--------|
| `MailConfigService` | 邮件配置和发送 | system |
| `SmsConfigService` | 短信配置和发送 | system |
| `DingTalkConfigService` | 钉钉配置和发送 | system |
| `SnmpConfigService` | SNMP Trap发送 | system |
| `SyslogConfigService` | Syslog消息发送 | system |
| `FtpConfigService` | FTP文件传输 | report |
| `SftpConfigService` | SFTP文件传输 | report |

## 数据模型

| 实体 | 说明 |
|------|------|
| DingTalkConfig | 钉钉机器人配置 |
| MailConfig | 邮件服务器配置 |
| SmsConfig | 短信网关配置 |
| SnmpConfig | SNMP Trap目标配置 |
| SyslogConfig | Syslog服务器配置 |
| FtpConfig | FTP服务器配置 |
| SftpConfig | SFTP服务器配置 |
| WebsocketConfig | WebSocket配置 |
| MsgPlatformConfig | 消息平台配置 |
