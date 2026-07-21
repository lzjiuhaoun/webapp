# ACCEPTANCE.md — daemon 模块验收

## 验收标准

### T1: 启动与注册
- [ ] 可正常启动（无需数据库连接）
- [ ] 启动后成功注册到 Zookeeper /services/daemon

### T2: 服务监控
- [ ] 可对所有配置的服务节点设置 Watch
- [ ] 监控的服务包括: reportengine,syslogserver,webapp,managercenter,kafkaclient,kafkaclient1~5
- [ ] Watch 机制正常工作

### T3: 异常检测与恢复
- [ ] 服务节点消失时 daemon 可检测到
- [ ] 检测到异常后执行重启命令
- [ ] 重启命令执行成功
- [ ] 异常事件有日志记录

### T4: 多节点并发
- [ ] 可同时 Watch 多个服务节点
- [ ] 多个节点同时消失时可分别处理

### T5: 系统监控
- [ ] 可获取 CPU 使用率
- [ ] 可获取内存使用率
- [ ] 可获取本机 IP 地址
