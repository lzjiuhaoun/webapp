# ACCEPTANCE.md — common-module 模块验收

## 验收标准

### T1: Zookeeper 功能
- [ ] 可成功连接 Zookeeper 服务器
- [ ] 可创建/删除/读写 ZK 节点
- [ ] Watcher 机制正常工作，节点变化可触发回调
- [ ] 服务注册后，其他客户端可发现

### T2: Redis 功能
- [ ] Redis 连接配置正确加载
- [ ] RedisUtil 可存取 String/List/Set/Hash 数据
- [ ] JSON 序列化/反序列化正确

### T3: SM4 加密
- [ ] 加密后的数据可正确解密
- [ ] 相同明文 + 相同 key 产生相同密文
- [ ] 不同 key 产生不同密文

### T4: 数据库备份
- [ ] 全量备份可成功执行
- [ ] 增量备份可成功执行
- [ ] 备份文件可选择 SM4 加密
- [ ] 备份文件可恢复

### T5: 工具类
- [ ] LinuxUtils 可执行 Shell 命令并返回结果
- [ ] DatabaseUrlParserUtil 可解析 MySQL/DM URL
- [ ] CommonBeanUtil 可正确获取 Bean
