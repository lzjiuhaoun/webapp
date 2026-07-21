# EXPORTS.md — common-module 出口契约

> 版本: 1.0.0

## 提供的服务/类

### 1. Zookeeper 客户端

**包**: com.ankki.common.zookeeper

| 类 | 用途 |
|------|------|
| `ZookeeperClient` | Zookeeper 客户端，封装连接、注册、Watch、数据操作 |
| `WatchAction` | Watch 回调动作，触发服务重启 |
| `WatcherCom` | Watcher 基类 |
| `WatcherProtector` | Watcher 保护，节点变化时重启服务 |

**核心方法**:
- `connectZk()` — 连接 Zookeeper
- `registerServerStandalone()` — 注册服务为临时节点
- `createNode(path, data)` — 创建节点
- `deleteNode(path)` — 删除节点
- `getData(path)` — 获取节点数据
- `setData(path, data)` — 设置节点数据
- `addWatch(path, WatchAction)` — 添加 Watch

### 2. Redis 工具

**包**: com.ankki.common.redis, com.ankki.common.util

| 类 | 用途 |
|------|------|
| `RedisConfig` | Redis 配置，Jackson2Json 序列化 |
| `RedisUtil` | Redis 操作工具类 |

**RedisUtil 核心方法**:
- `set(key, value)` — 设置 String
- `get(key)` — 获取 String
- `hmset(key, map)` — 设置 Hash
- `hget(key, field)` — 获取 Hash
- `sAdd(key, values)` — 添加 Set
- `lPush(key, values)` — 添加 List
- `zAdd(key, score, value)` — 添加 ZSet
- `del(key)` — 删除 Key

### 3. SM4 加密

**包**: com.ankki.common.util

| 类 | 用途 |
|------|------|
| `SM4Util` | SM4 对称加密工具 |

**核心方法**:
- `encrypt(content, key)` — 加密
- `decrypt(content, key)` — 解密
- 工作模式: CBC, PKCS5Padding, 128-bit key

### 4. 数据库备份

**包**: com.ankki.common.backup

| 类 | 用途 |
|------|------|
| `AbstractMysqlBackupManager` | MySQL 备份抽象类，支持全量/增量备份 |
| `DatasourceBackup` | 备份接口 |
| `BackupHistory` | 备份历史模型 |
| `BackupPolicy` | 备份策略模型 |

### 5. Linux 工具

**包**: com.ankki.common.util

| 类 | 用途 |
|------|------|
| `LinuxUtils` | Linux 命令执行 |

**核心方法**:
- `executeLinuxCommand(cmdStr, cmdStrArr)` — 执行 Shell 命令

### 6. 通用工具

**包**: com.ankki.common.util

| 类 | 用途 |
|------|------|
| `CommonBeanUtil` | Spring Bean 工具，EnumSingleton 单例模式 |
| `DatabaseUrlParserUtil` | JDBC URL 解析 |
| `Constants` | 全局常量 |
