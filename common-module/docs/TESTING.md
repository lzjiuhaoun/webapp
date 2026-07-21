# TESTING.md — common-module 测试策略

## 测试工具链

- JUnit 4 + Spring Boot Test
- Mockito（Mock 外部依赖）

## 测试目录结构

```
src/test/java/com/ankki/common/
├── zookeeper/      # ZookeeperClient 测试
├── util/           # 工具类测试
├── redis/          # RedisUtil 测试
└── backup/         # 备份功能测试
```

## Mock 边界

- Zookeeper 连接：Mock Zookeeper 服务端
- Redis 连接：使用 Embedded Redis 或 Mock
- 文件系统：Mock LinuxUtils 命令执行

## 测试重点

1. SM4Util 加解密可逆性
2. ZookeeperClient 节点 CRUD 正确性
3. RedisUtil 数据存取一致性
4. DatabaseUrlParserUtil URL 解析覆盖
5. Watcher 机制触发服务重启
