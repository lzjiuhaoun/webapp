# 应用启动指南

## 环境要求

| 组件 | 版本 | 说明 |
|------|------|------|
| JDK | 1.8+ | 强制 Java 8，禁止 Java 11+ 特性 |
| Maven | 3.6+ | 构建工具 |
| DM8 | 8.1 | 达梦数据库，大小写敏感模式，数据库名 `SCM_RJ_SIMP` |
| Node.js | 16+ | 前端开发环境 |
| npm | 8+ | 前端包管理 |

## 项目路径

```
项目根目录: E:\文档-项目\【综治】BXY\看板B02
```

---

## 一、数据库初始化

在 DM8 `SCM_RJ_SIMP` 数据库中执行建表脚本。

**连接信息**：`172.19.5.59:5236` / `SYSDBA` / `SYSDBA` / 数据库 `SCM_RJ_SIMP`

**脚本位置**：`E:\文档-项目\【综治】BXY\看板B02\sql\init-alert-access-list.sql`

### 方式一：DM 管理工具

使用 DM Manager 打开脚本文件直接执行。

### 方式二：disql 命令行

```bash
cd /opt/dmdbms/bin
./disql SYSDBA/SYSDBA@172.19.5.59:5236
`start E:\文档-项目\【综治】BXY\看板B02\sql\init-alert-access-list.sql`
```

### 方式三：无客户端时通过 JDBC 执行

如果当前机器上没有 DM 客户端工具，可使用项目中的 DM JDBC 驱动直接执行：

```bash
# 编译 runner
cd E:\文档-项目\【综治】BXY\看板B02\web-app\lib
javac -cp DmJdbcDriver18.jar C:\Users\liuzhaojun\AppData\Local\Temp\opencode\DmSqlRunner.java -d C:\Users\liuzhaojun\AppData\Local\Temp\opencode

# 执行建表脚本
java -cp "DmJdbcDriver18.jar;C:\Users\liuzhaojun\AppData\Local\Temp\opencode" DmSqlRunner "jdbc:dm://172.19.5.59:5236/SCM_RJ_SIMP" SYSDBA SYSDBA "E:\文档-项目\【综治】BXY\看板B02\sql\init-alert-access-list.sql"
```

### 注意事项

- DM8 **不支持** `DEFAULT NOW()`，时间戳改用 `BIGINT` 由 Java 层 `System.currentTimeMillis()` 赋值
- DM8 **不支持** `DROP TABLE IF EXISTS`，需直接 `DROP TABLE <表名>`
- 建表脚本使用大写标识符，匹配 DM8 大小写敏感模式

---

## 二、DmJdbcDriver18.jar 配置

DM8 JDBC 驱动为私有 JAR，需要手动放入：

```
E:\文档-项目\【综治】BXY\看板B02\web-app\lib\DmJdbcDriver18.jar
```

从 DM8 安装目录 `$DM_HOME/drivers/jdbc/DmJdbcDriver18.jar` 复制到上述路径。

---

## 三、后端启动

### 3.1 修改数据库连接

编辑连接配置文件：

```
E:\文档-项目\【综治】BXY\看板B02\web-app\src\main\resources\application-dev.yml
```

确认其中的连接信息与实际 DM8 一致：

```yaml
spring:
  datasource:
    druid:
      url: jdbc:dm://172.19.5.59:5236/SCM_RJ_SIMP
      username: SYSDBA
      password: SYSDBA
```

### 3.2 Maven 编译 + 启动

```bash
# 进入项目根目录
cd E:\文档-项目\【综治】BXY\看板B02

# 编译并启动 (注意: 不要加 -am 参数)
mvn clean spring-boot:run -pl web-app
```

首次启动会下载依赖（Spring Boot 2.5.15、MyBatis、Druid 等），需保持网络畅通。

### 3.3 验证启动

启动日志应包含以下关键信息：

```
Tomcat started on port(s): 8543 (http)
Parsed mapper file: '.../dm/mapper/AccessListMapper.xml'
Started WebAppApplication in 2.6 seconds
```

### 3.4 停止

```bash
# 方案一：按 Ctrl + C (前台启动)
# 方案二：查找 PID 后 kill
netstat -ano | findstr ":8543"
taskkill /PID <PID> /F
```

### 3.5 以 WAR 包部署 (生产环境)

```bash
# 进入项目根目录
cd E:\文档-项目\【综治】BXY\看板B02

# 构建 WAR
mvn clean package -pl web-app -DskipTests

# WAR 包输出位置
E:\文档-项目\【综治】BXY\看板B02\web-app\target\webapp.war
```

---

## 四、API 验证

后端启动后验证接口：

```bash
# 分页查询黑白名单列表
curl http://localhost:8543/alert-access-list?pageNo=1&pageSize=10

# 预期返回格式
# {"code":"000000000","message":"操作成功","data":{...}}
```

常见错误处理：

| 现象 | 原因 | 解决 |
|------|------|------|
| 500 + "无效的表名或视图" | 未执行建表脚本 | 执行 `E:\文档-项目\【综治】BXY\看板B02\sql\init-alert-access-list.sql` |
| 400 + "违反 [ACCESS_LIST] 唯一约束" | 数据冲突 | 检查已存在的数据 |
| 启动时 Port 8543 is already in use | 端口被占用 | `taskkill /F /PID <占用端口的PID>` |
| 前端 Module not found: core-js/modules/es.xxx | `core-js@2` 在顶层，Vue CLI 5 需要 `core-js@3` | `npm install core-js@^3.49.0` 添加为顶层依赖 |
| 前端 cannot access http://localhost:8081 | dev server 被终端超时杀死 | 另开终端窗口运行 `npm run dev`，不要混用 |

---

## 五、前端启动

```bash
# 进入前端目录
cd E:\文档-项目\【综治】BXY\看板B02\web-app\frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

访问 `http://localhost:8081/#/alert/access-list` 进入黑白名单配置页面。

前端开发服务器已配置代理（`E:\文档-项目\【综治】BXY\看板B02\web-app\frontend\vue.config.js`），API 请求自动转发到后端 `localhost:8543`。

### 构建部署

```bash
cd E:\文档-项目\【综治】BXY\看板B02\web-app\frontend
npm run build
# 产物输出到 E:\文档-项目\【综治】BXY\看板B02\web-app\frontend\dist\
```
