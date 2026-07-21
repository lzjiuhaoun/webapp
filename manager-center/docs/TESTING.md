# TESTING.md — manager-center 测试策略

## 测试工具链

- JUnit 4 + Spring Boot Test
- Mockito — Mock MyBatis Mapper、Socket 连接、Linux 命令执行
- Embedded TCP Server — 测试 Socket 消息收发
- H2 Database — 替代 MySQL 进行 Mapper 测试

## 测试目录结构

```
src/test/java/com/ankki/managercenter/
├── server/             # ServerSocketService, ManagementCenterMessHandler 测试
├── client/             # SocketClient 测试
├── config/             # GlobalConfiguration, SystemConfig 测试
├── dao/                # MessageRoutingMapper 测试
├── model/              # MessageRouting 模型测试
├── util/               # BeanUtil, LinuxUtil 测试
└── ManagerCenterApplicationTest  # 启动测试
```

## Mock 边界

| 外部依赖 | Mock 方式 | 说明 |
|---------|----------|------|
| MyBatis Mapper | Mockito Mock `selectObjByParams()` | 返回预定义路由列表 |
| Socket 连接 | Mockito Mock `initSocket()` | 测试路由逻辑 |
| Zookeeper | Curator Test Server 或 Mock | 测试服务注册 |
| Linux 命令 | Mockito Mock `executeLinuxCommand()` | 测试重启脚本调用 |
| TCP 连接 | Embedded TCP Server 或 Mock | 测试消息收发 |

## 测试重点

1. **消息解析正确性**：管道分隔格式 `messageType|contentType|...` 解析
2. **IP 验证**：仅允许 127.0.0.1，其他 IP 被丢弃
3. **路由表查询**：`selectObjByParams(messageType, contentType)` 查询正确
4. **广播转发**：N 条匹配时转发到所有目标
5. **特殊消息**：类型 4，内容 1 触发重启命令
6. **Socket 客户端**：initSocket/sendMessage/releaseResource 流程
7. **线程池配置**：core=6, max=6, queue=50
8. **资源清理**：`releaseSource()` 在 finally 块中执行
9. **异常容错**：转发失败不阻塞其他路由
10. **BeanUtil 访问**：EnumSingleton 获取 Spring Bean
