# INTERNAL_LOGIC.md — common-module 内部实现逻辑

> 本模块为公共库，不独立运行。

## Zookeeper 客户端实现

- 使用 Zookeeper 3.4.14 原生 API
- `ZookeeperClient` 维护单例连接，连接超时 30s
- Watcher 机制：通过 `WatcherProtector` 包装原生 Watcher，防止 Watcher 一次性失效
- 服务注册路径：`/services/{serviceName}`，存储内容 `{host}:{port}`
- 节点类型：服务节点使用 `EPHEMERAL`（临时节点），进程退出自动删除
- Watch 事件：NodeCreated、NodeDeleted、NodeDataChanged，触发 `WatchAction` 重启服务

## Redis 配置实现

- 使用 Spring Data Redis + Lettuce 客户端
- `RedisConfig` 配置 RedisTemplate，使用 Jackson2JsonRedisSerializer 序列化
- `RedisUtil` 封装 String/List/Set/Hash/ZSet 常用操作
- 连接配置：host=localhost, port=6379, password 从 ZookeeperConfig 加载

## SM4 加密实现

- 基于 BouncyCastle 库的国密 SM4 算法
- 工作模式：CBC，PKCS5Padding
- Key 长度：128bit（16 字节）
- 返回值：Base64 编码字符串

## 数据库备份实现

- `AbstractMysqlBackupManager` 封装 mysqldump 命令执行
- 全量备份：导出所有表结构和数据
- 增量备份：基于 binlog 位置点
- 备份文件可选 SM4 加密压缩
- 备份策略通过 BackupPolicy 配置

## Linux 工具实现

- `LinuxUtils.executeLinuxCommand()` 使用 Runtime.getRuntime().exec() 执行命令
- 使用双线程读取标准输出和错误输出
- 编码：GB2312

## CommonBeanUtil 实现

- 实现 ApplicationContextAware，获取 Spring ApplicationContext
- EnumSingleton 单例模式，非 Spring Bean 上下文中获取 Bean
- 提供 getMessage() 国际化支持
