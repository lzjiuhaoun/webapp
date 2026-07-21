# TASKS.md — collection-engine 局部任务书

## 阶段 1: Netty UDP 服务器

### 任务 1.1: NettyUdpServer 启动
- [ ] 创建 NioEventLoopGroup 事件循环
- [ ] 创建 Bootstrap 绑定 NioDatagramChannel
- [ ] 设置 handler 为 NettyUdpChannelInHandler
- [ ] 配置 SO_BROADCAST、SO_RCVBUF、SO_SNDBUF
- [ ] 绑定 simp:9514 端口
- [ ] 阻塞等待连接

### 任务 1.2: NettyUdpChannelInHandler
- [ ] channelRead0 接收 DatagramPacket
- [ ] 提取 IP、端口、数据（UTF-8）
- [ ] 检查 nettyDataHandler 开关
- [ ] 调用 NettySyslogService.dataHandler（异步）

## 阶段 2: Syslog 数据解析与分类

### 任务 2.1: NettySyslogService
- [ ] dataHandler 方法（异步）
- [ ] 判断数据类型（happenTime/record_id/alarmType）
- [ ] 查询设备配置（SafetyDevConfigMapper）
- [ ] 分类到 noRiskList/riskList/dbConfList/dbFlowList
- [ ] 批量检查与 Kafka 生产

### 任务 2.2: Sangfor 格式转换
- [ ] getSXFString 方法
- [ ] 提取日期、record_time、src_ip、src_port 等字段
- [ ] 查询数据库类型 ID
- [ ] 转换为标准格式，设置 dataType=1

### 任务 2.3: 告警数据处理
- [ ] 识别 alarmType（脱敏告警）
- [ ] 识别 DBAudit（防火墙告警）
- [ ] 按 IP 和 dev_type_id=5 查询设备
- [ ] 设置 dataType=9，发送告警

## 阶段 3: Kafka 生产

### 任务 3.1: NettyKafkaProduce
- [ ] 构造函数创建 KafkaProducer（SASL_PLAINTEXT）
- [ ] 配置 acks=all, retries=0, batch.size=16384
- [ ] run 方法：批量发送或逐条发送
- [ ] Thread.sleep(5) 后关闭 producer

### 任务 3.2: KafkaProduce（Legacy）
- [ ] 构造函数创建 KafkaProducer
- [ ] 线程池配置（core=5, max=5, queue=150）
- [ ] run 方法：批量发送或逐条发送
- [ ] Thread.sleep(100) 后关闭 producer

## 阶段 4: 设备配置管理

### 任务 4.1: SafetyDevConfigMapper
- [ ] selectSyslogDevBySrcIpAndPort 查询
- [ ] selectSyslogDevBySrcIpAndDevType 查询
- [ ] selectDbType 查询

### 任务 4.2: TopicRelationInfoMapper
- [ ] selectByLogType 查询 Topic 映射
- [ ] 关联 dev_data_type 表

## 阶段 5: 服务集成

- [ ] SimpsyslogserverApplication 启动
- [ ] Zookeeper 服务注册
- [ ] ProGuard 混淆打包
- [ ] AppAssembler + tar.gz 打包
