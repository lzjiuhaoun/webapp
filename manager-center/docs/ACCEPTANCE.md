# ACCEPTANCE.md — manager-center 模块验收

## 验收标准

### T1: Socket 服务
- [ ] 可在 9090 端口启动 Socket 服务
- [ ] 可接受本地（127.0.0.1）连接
- [ ] 仅允许 127.0.0.1 连接，远程连接被丢弃
- [ ] 线程池（core=6, max=6）正常工作
- [ ] 并发连接处理正常

### T2: 消息解析
- [ ] 管道分隔格式 `messageType|contentType|operType|operNum|uuids|timestampMill` 解析正确
- [ ] 可提取 `messageType` 和 `contentType` 用于路由
- [ ] 剩余字段原样转发

### T3: 路由转发
- [ ] 路由表查询 `selectObjByParams(messageType, contentType)` 正确
- [ ] 0 条匹配时消息被丢弃并记录日志
- [ ] 1 条匹配时转发到目标 host:port
- [ ] N 条匹配时广播到所有目标
- [ ] 转发失败（连接失败）时静默处理，不影响其他路由
- [ ] 每条连接独立：connect → send → close

### T4: 特殊消息
- [ ] `messageType=4, contentType=1` 触发系统重启脚本
- [ ] 执行命令: `cd /home/simp/conf/client; sh start_all_service.sh`
- [ ] 特殊消息不查询路由表，不转发
- [ ] 执行后记录日志 "重启所有的组件与服务成功"

### T5: Socket 客户端
- [ ] `SocketClient.initSocket()` 可成功建立连接
- [ ] `SocketClient.sendMessage()` 可发送管道分隔消息
- [ ] `SocketClient.releaseResource()` 可关闭连接
- [ ] 无重试逻辑（连接失败返回 false）

### T6: 资源清理
- [ ] `releaseSource()` 在 `finally` 块中保证执行
- [ ] 关闭 DataInputStream, DataOutputStream, Socket

### T7: 服务注册
- [ ] 启动后成功注册到 Zookeeper `/services/managercenter`
- [ ] 注册数据为 `localhost:9090`
- [ ] 节点类型为 `EPHEMERAL`

### T8: 异常处理
- [ ] Accept 循环中 IOException 被捕获，循环继续
- [ ] 路由表中无匹配时记录日志，消息丢弃
- [ ] 转发失败时记录日志，继续下一个路由

### T9: 配置加载
- [ ] `application.yml` 配置正确加载（数据源、Socket、线程池、MyBatis）
- [ ] Druid 连接池配置生效（initial-size=5, maxActive=20）
- [ ] MyBatis Mapper 扫描正确（`classpath:mapper/*.xml`）
