# TESTING.md — web-app 测试策略

## 测试工具链

- JUnit 4 + Spring Boot Test
- Mockito — Mock Service、Mapper、外部服务
- MockMvc — REST API 测试
- Embedded Redis / H2 — 替代数据库

## 测试目录结构

```
src/test/java/com/ankki/
├── controller/           # Controller 测试
│   ├── user/             # 用户相关
│   ├── system/           # 系统相关
│   ├── data/             # 数据相关
│   ├── risk/             # 风险相关
│   └── ...               # 其他 Controller
├── service/              # Service 层测试
└── config/               # 配置测试
```

## Mock 边界

- ES 查询：Mock 返回测试数据
- MySQL：使用 H2 内存数据库
- Kafka：Mock Producer/Consumer
- CAS：Mock 认证流程
- Socket 通信：Mock
- Zookeeper：Mock
- 外部数据库连接：Mock

## 测试重点

1. REST API 端点响应正确性（MockMvc）
2. CAS 认证流程
3. API 加密/解密
4. WebSocket 连接和消息推送
5. 文件上传下载
6. PDF/Excel 导出
7. 跨域配置
8. 70+ Controller 覆盖核心路径
