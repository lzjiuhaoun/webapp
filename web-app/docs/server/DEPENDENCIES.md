# DEPENDENCIES.md — server 服务器配置模块

## 外部模块依赖

| 依赖模块 | 依赖内容 | 版本约束 |
|----------|---------|----------|
| 无 | 本模块为底层服务，不依赖其他业务模块 | — |

## 外部库依赖

| 库 | 用途 |
|----|------|
| org.springframework.boot:spring-boot-starter-mail | 邮件发送 |
| org.springframework.boot:spring-boot-starter-thymeleaf | 邮件模板 |
| org.apache.camel:camel-ftp | FTP/SFTP |
| org.snmp4j:snmp4j | SNMP Trap |
| org.syslog4j:syslog4j | Syslog |
| com.ankki:common-module | 公共工具类 |
