# EXPORTS.md — daemon 出口契约

> 版本: 1.0.0

## 提供的服务

| 服务 | 说明 |
|------|------|
| 进程健康监控 | 通过 ZK Watch 监控所有注册服务 |
| 自动重启 | 检测到服务异常后执行重启脚本 |
| 系统监控 | Linux CPU/内存/网络监控 |

## 监控的服务列表

| 服务名 | ZK 路径 | 说明 |
|--------|---------|------|
| reportengine | /services/reportengine | 报表引擎 |
| syslogserver | /services/syslogserver | 采集引擎 |
| webapp | /services/webapp | Web 应用 |
| managercenter | /services/managercenter | 管理中心 |
| kafkaclient | /services/kafkaclient | Kafka 客户端 |
| kafkaclient1~5 | /services/kafkaclient1~5 | Kafka 客户端集群 |

## 服务注册

- Zookeeper 路径: `/services/daemon`
- 注册数据: `{host}:{port}`
- 节点类型: `EPHEMERAL`（进程退出自动删除）

## 系统监控接口

| 方法 | 说明 |
|------|------|
| `getCPUUsage()` | 获取 CPU 使用率 |
| `getMemoryUsage()` | 获取内存使用率 |
| `getLocalIP()` | 获取本机 IP |
| `executeLinuxCommand(cmd)` | 执行 Linux 命令 |
