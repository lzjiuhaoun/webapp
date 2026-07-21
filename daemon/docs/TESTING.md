# TESTING.md — daemon 测试策略

## 测试工具链

- JUnit 4 + Spring Boot Test
- Mockito（Mock Zookeeper）
- Curator Test Server（嵌入式 Zookeeper）

## 测试目录结构

```
src/test/java/com/ankki/daemon/
├── DaemonApplicationTest   # 启动测试
└── util/                   # 工具类测试
```

## Mock 边界

- Zookeeper：使用 Curator Test Server 或 Mock
- Linux 命令执行：Mock LinuxUtils
- 重启脚本：Mock 脚本执行

## 测试重点

1. 启动后可注册到 Zookeeper
2. Watch 可正确设置到目标节点
3. 节点消失事件可触发重启逻辑
4. 重启命令可正确执行
5. 多节点并发 Watch 正常
6. 系统监控功能（CPU/内存/IP）
