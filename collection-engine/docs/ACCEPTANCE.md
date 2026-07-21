# ACCEPTANCE.md — collection-engine 模块验收

## 验收标准

### T1: UDP 服务器
- [ ] Netty UDP 服务器可启动并绑定 9514 端口
- [ ] 可接收 UDP 数据包
- [ ] 可解析 UTF-8 字符串数据
- [ ] 支持广播（SO_BROADCAST=true）
- [ ] 接收缓冲区 10MB，发送缓冲区 10MB

### T2: 数据解析
- [ ] 标准 Syslog JSON 数据可正确解析为 CollectionModel
- [ ] Sangfor 设备数据可正确转换为标准格式
- [ ] 告警数据（alarmType/DBAudit）可正确识别
- [ ] 字段提取完整（srcIp, destIp, dbType, operSentence 等）

### T3: 数据分类
- [ ] dataType=0 的非风险日志可正确分类
- [ ] dataType=1 的风险日志可正确分类
- [ ] dataType=2 的资产配置日志可正确分类
- [ ] dataType=3 的资产流量日志可正确分类
- [ ] dataType=9 的告警日志可正确分类
- [ ] 设备 collectionDataType 过滤正确

### T4: Kafka 生产
- [ ] 可连接到 Kafka（SASL_PLAINTEXT 认证）
- [ ] 批量数据（0,1,3）可发送为单条消息
- [ ] 逐条数据（2）可逐条发送
- [ ] 告警数据（9）可发送为 JSON 字符串
- [ ] 批量阈值达到后自动发送（riskMaxNum=1 等）
- [ ] 发送后 Thread.sleep(5) 间隔

### T5: 设备配置管理
- [ ] 可按 IP+端口查询设备配置
- [ ] 可按 IP+设备类型查询设备配置
- [ ] Topic 映射表查询正确
- [ ] 数据库类型 ID 查询正确

### T6: Sangfor 格式转换
- [ ] 日期字段可转换为 Unix 秒
- [ ] 源/目标 IP、端口可正确提取
- [ ] 数据库类型可查询并转换
- [ ] 操作语句、操作类型可提取
- [ ] 影响行数、处理状态可提取

### T7: 告警数据处理
- [ ] alarmType 告警可识别并发送
- [ ] DBAudit 防火墙告警可识别并发送
- [ ] 告警数据 dataType=9，发送 topic 正确

### T8: 线程池
- [ ] 线程池 core=5, max=5, queue=150
- [ ] AbortPolicy 拒绝策略生效
- [ ] 线程名格式正确

### T9: 服务注册
- [ ] 启动后成功注册到 Zookeeper /services/syslogserver
- [ ] 注册数据正确
