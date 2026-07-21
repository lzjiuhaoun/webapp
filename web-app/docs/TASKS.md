# TASKS.md — web-app 局部任务书

## 开发任务

### 阶段 1: 基础框架
- [ ] WebAppApplication 启动配置
- [ ] Tomcat HTTPS 配置（8443）
- [ ] HTTP 到 HTTPS 重定向（8543）
- [ ] 404 重定向到 index.html（Vue SPA）
- [ ] CORS 跨域配置

### 阶段 2: 认证系统
- [ ] CAS SSO 集成
- [ ] LoginController 登录接口
- [ ] UserController 用户管理
- [ ] Token 生成和验证

### 阶段 3: 核心 Controller
- [ ] SearchController 审计日志检索
- [ ] BiTaskController / ItaTaskController 报表任务
- [ ] DbTaskDiscoveryController 数据库发现
- [ ] DbAssetsController 资产管理
- [ ] SituationPredictController 态势预测
- [ ] Risk 相关 Controller（操作风险、数据访问风险、攻击风险等）
- [ ] SensData 相关 Controller（敏感数据管理）

### 阶段 4: 配置管理
- [ ] 告警策略配置
- [ ] 安全规则配置
- [ ] 审计策略配置
- [ ] 组织架构和逻辑区域
- [ ] 通知配置（邮件、短信、钉钉等）

### 阶段 5: 系统维护
- [ ] 系统升级
- [ ] 备份策略和历史
- [ ] 许可证管理
- [ ] 部署管理

### 阶段 6: 高级功能
- [ ] WebSocket 推送
- [ ] Quartz 定时调度
- [ ] 文件管理（FTP/SFTP）
- [ ] PDF/Excel 报表导出
- [ ] 拓扑图
- [ ] 大屏监控
- [ ] API 加密（SM2/RSA）

### 阶段 7: 服务集成
- [ ] Zookeeper 服务注册和 Watch
- [ ] Socket Server（9095 端口）
- [ ] Swagger API 文档
