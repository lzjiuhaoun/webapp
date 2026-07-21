# TESTING.md — db-assets 数据库资产模块

## 测试策略

| 测试类型 | 工具 | 范围 |
|---------|------|------|
| 单元测试 | JUnit 5 | Service 层方法 |
| 集成测试 | Spring Boot Test | Controller + 数据库 |

## Mock 边界

- 多数据库连接 → Mock DatabaseFactory
- Quartz 任务 → 直接调用目标方法
- 外部设备通信 → Mock device Service

## 已有测试

| 测试类 | 路径 |
|--------|------|
| DbAssetsControllerTest | `test/java/.../controller/dbassets/DbAssetsControllerTest.java` |
