# INTERNAL_LOGIC.md — web-app 内部实现逻辑

> 本模块描述 web-app 的内部实现细节。

## 启动流程

1. `WebAppApplication.main()` 启动
2. 排除 MongoAutoConfiguration，启用 @EnableCaching 和 @EnableAsync
3. 配置 Tomcat：HTTP 8543 → HTTPS 8443 重定向
4. 配置 404/403 重定向到 index.html（Vue SPA 支持）
5. 注册 webapp 到 Zookeeper
6. Watch ZK 的 daemon1 服务节点

## Controller 架构

- 所有 Controller 继承 BaseController
- BaseController 默认启用 @CrossOrigin 跨域
- 所有 Controller 使用 @RestController 注解
- 请求参数通过 @RequestParam / @RequestBody 接收
- 响应统一包装格式

## CAS 认证流程

1. 未认证请求拦截，重定向到 CAS 服务器
2. CAS 认证成功后返回 TGT/Ticket
3. web-app 验证 Ticket，建立本地会话
4. 提供 /getToken 接口获取 JWT token
5. 后续请求携带 token 进行身份验证

## API 加密

- 请求体：客户端使用服务端公钥（SM2/RSA）加密
- 响应体：服务端使用客户端公钥加密
- 加解密拦截器在 Controller 前后执行

## Quartz 调度

- JDBC 模式，任务状态存储在 MySQL
- 集群模式支持
- 用于定时报表生成、数据同步等

## 文件管理

- FTP/SFTP 通过 Apache Camel FTP
- 文件上传使用 commons-fileupload
- 支持文件目录结构管理

## 报表导出

- PDF：使用 iText 5.5.13.3
- Excel：使用 Apache POI 5.2.2
- 报表模板预定义

## 多数据库支持

- 支持 MySQL、Oracle、PostgreSQL、DB2、SQL Server、Sybase、Informix、DM、Kingbase、GBase、MongoDB、Hive
- 通过 JDBC 连接池管理
- 数据库发现功能自动扫描数据库实例

## 数据加密

- SM2/RSA 非对称加密（BouncyCastle）
- AES 对称加密
- 敏感数据脱敏处理
