# TESTING.md — collection-engine 测试策略

## 测试工具链

- JUnit 4 + Spring Boot Test
- Mockito — Mock Kafka Producer、MySQL Mapper、Zookeeper
- Embedded Kafka — 测试 Kafka 生产
- Embedded MySQL — 替代数据库进行 Mapper 测试
- Netty 嵌入式测试 — 测试 UDP 服务器

## 测试目录结构

```
src/test/java/com/ankki/simpsyslogserver/
├── service/          # NettySyslogService, NettyUdpServer 测试
├── util/             # NettyKafkaProduce, KafkaUtils 测试
├── dao/              # Mapper 测试
├── model/            # CollectionModel, DataFormat 测试
└── SimpsyslogserverApplicationTest  # 启动测试
```

## Mock 边界

| 外部依赖 | Mock 方式 | 说明 |
|---------|----------|------|
| Kafka Producer | Mockito Mock | 测试消息发送 |
| MySQL Mapper | Mockito Mock | 返回预定义设备配置 |
| Zookeeper | Curator Test Server 或 Mock | 测试服务注册 |
| UDP 端口 | Embedded UDP Server | 测试数据接收 |

## 测试重点

1. **Netty UDP 服务器**：启动、绑定端口、接收数据
2. **数据解析**：标准 Syslog JSON、Sangfor 格式、告警格式
3. **数据分类**：按 dataType (0,1,2,3,9) 正确分类
4. **设备配置查询**：按 IP+端口、IP+设备类型查询
5. **Topic 映射**：数据库查询 Topic 正确
6. **Kafka 生产**：批量发送、逐条发送、SASL 认证
7. **批量阈值**：达到阈值后发送，清空列表
8. **Sangfor 格式转换**：字段提取和转换正确
9. **告警数据处理**：alarmType 和 DBAudit 识别
10. **线程池**：core=5, max=5, queue=150 配置
