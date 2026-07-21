# TASKS.md — daemon 局部任务书

## 开发任务

### 阶段 1: 启动与注册
- [ ] DaemonApplication 启动配置
- [ ] 排除 DataSourceAutoConfiguration
- [ ] Zookeeper 服务注册

### 阶段 2: 服务监控
- [ ] 遍历 watch.services 配置
- [ ] 对每个服务节点设置 ZK Watch
- [ ] Watcher 回调实现

### 阶段 3: 异常处理
- [ ] NodeDeleted 事件处理
- [ ] 日志记录异常服务
- [ ] 重启命令执行

### 阶段 4: 工具类
- [ ] LinuxUtils Shell 命令执行
- [ ] 获取 CPU/内存使用率
- [ ] 获取本机 IP

### 阶段 5: 打包
- [ ] ProGuard 混淆配置
- [ ] AppAssembler 打包
