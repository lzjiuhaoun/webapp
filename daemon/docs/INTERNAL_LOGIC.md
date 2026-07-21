# INTERNAL_LOGIC.md — daemon 内部实现逻辑

> 本模块描述 daemon 的内部实现细节。

## 启动流程

1. `DaemonApplication.main()` 启动
2. 排除 DataSourceAutoConfiguration（无需数据库连接）
3. 连接 Zookeeper（host: `127.0.0.1:10000`）
4. 注册 daemon 到 Zookeeper，创建临时节点
5. 遍历 watch.services 配置中的服务列表
6. 对每个服务节点设置 ZK Watch

## 监控机制

- 使用 common-module 的 `ZookeeperClient` 连接 Zookeeper
- 对每个 `/services/{serviceName}` 节点设置 Watcher
- Watcher 类型：`NodeDeleted`（节点消失）
- 节点消失事件触发异常处理

## 自动恢复

- 检测到服务节点消失后
- 通过 common-module 的 `LinuxUtils` 执行重启脚本
- 重启脚本路径：`/home/simp/conf/client/{serviceName}.sh restart`
- 重启后等待服务重新注册到 ZK

## 系统监控

- `LinuxUtils.executeLinuxCommand()` 执行 top 命令获取 CPU 和内存使用率
- 解析 top 输出，提取 CPU% 和 Memory%
- 获取本机 IP 地址

## 配置

- `service.name=daemon`
- `service.host=127.0.0.1:10000`
- `watch.services=reportengine,syslogserver,webapp,managercenter,matchAnalysis,behaviorAnalysis,kafkaclient,kafkaclient1,...`
