# RustDesk API (Java Implementation)

基于 Spring Boot 3.2.5 + Maven 的 RustDesk API 服务器 Java 实现版本。

## 项目概述

这是一个完整的 RustDesk 远程桌面管理后端 API 系统,使用 Java + Spring Boot 框架开发,实现了与 Go 版本相同的功能。

## 技术栈

- **Java**: 21
- **Spring Boot**: 3.2.5
- **Spring Security**: 6.x (JWT + OAuth2 + LDAP)
- **Spring Data JPA**: Hibernate 6.4.4
- **数据库**: SQLite (开发) / MySQL / PostgreSQL (生产)
- **API文档**: Springdoc OpenAPI (Swagger)
- **构建工具**: Maven 3.x

## 主要功能

### PC端API
- ✅ 个人版API支持
- ✅ 用户认证(密码登录)
- ✅ JWT Token管理
- ✅ 地址簿管理
- ✅ 群组管理
- ✅ OAuth授权登录 (GitHub, Google, OIDC)
- ⚠️ LDAP集成 (需启用依赖)
- ✅ 国际化支持

### Web Admin 后台
- ✅ 用户管理
- ✅ 设备管理
- ✅ 地址簿管理
- ✅ 标签管理
- ✅ 群组管理
- ✅ OAuth配置管理
- ✅ 登录日志
- ✅ 连接审计日志
- ✅ 文件传输日志

### 安全特性
- ✅ BCrypt密码加密
- ✅ JWT Token认证
- ✅ 双重认证机制 (API + Admin)
- ✅ CORS跨域支持
- ✅ 基于角色的访问控制

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+
- SQLite (开发) / MySQL 8+ / PostgreSQL 13+ (生产)

### 安装运行

#### 1. 克隆项目

```bash
cd /path/to/your/workspace
# 项目已创建在 rustdesk-api-java 目录
```

#### 2. 配置数据库

**开发环境 (SQLite)**:
默认配置已设置为使用SQLite,无需额外配置。

**生产环境 (MySQL)**:
编辑 `src/main/resources/application-prod.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rustdesk?serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

#### 3. 编译项目

```bash
mvn clean compile
```

#### 4. 运行应用

```bash
# 开发环境
mvn spring-boot:run

# 或编译后运行
mvn clean package
java -jar target/rustdesk-api.jar
```

#### 5. 访问应用

- **API 端点**: http://localhost:21114/api
- **Admin 后台**: http://localhost:21114/_admin/
- **Swagger 文档**: http://localhost:21114/swagger-ui.html

### 默认管理员账号

初次启动时会自动创建管理员账号:
- **用户名**: `admin`
- **密码**: `admin@123`

⚠️ **请在首次登录后立即修改密码!**

## 项目结构

```
rustdesk-api-java/
├── src/main/java/com/rustdesk/api/
│   ├── config/              # 配置类
│   │   ├── SecurityConfig.java
│   │   ├── JpaConfig.java
│   │   └── properties/
│   ├── entity/              # 实体类 (15个)
│   │   ├── User.java
│   │   ├── Peer.java
│   │   ├── AddressBook.java
│   │   └── ...
│   ├── repository/          # JPA Repository (15个)
│   ├── service/             # 业务逻辑层 (7个)
│   │   ├── UserService.java
│   │   ├── UserTokenService.java
│   │   └── ...
│   ├── controller/          # 控制器
│   │   ├── api/            # RustDesk客户端API
│   │   └── admin/          # 管理后台API
│   ├── security/            # 安全相关
│   │   ├── filter/         # 认证过滤器
│   │   └── handler/        # 异常处理
│   ├── dto/                # 数据传输对象
│   │   ├── request/
│   │   └── response/
│   ├── util/               # 工具类
│   └── exception/          # 异常定义
├── src/main/resources/
│   ├── application.yml           # 主配置
│   ├── application-dev.yml       # 开发环境
│   ├── application-prod.yml      # 生产环境
│   └── db/migration/            # Flyway数据库迁移
│       ├── V1__init_schema.sql
│       └── V2__init_data.sql
├── pom.xml
└── README.md
```

## 配置说明

### application.yml 主要配置

```yaml
server:
  port: 21114

rustdesk:
  jwt:
    secret: ${JWT_SECRET:}      # JWT密钥(为空则使用MD5 token)
    expiration: 604800          # 7天

  security:
    captcha-threshold: 3        # 验证码触发次数
    ban-threshold: 5            # 封禁IP触发次数

  rustdesk-server:
    id-server: ""               # RustDesk ID服务器
    relay-server: ""            # RustDesk Relay服务器
    api-server: "http://localhost:21114"
    key: ""                     # 公钥
```

### 环境变量

| 变量名 | 说明 | 示例 |
|-------|------|------|
| `JWT_SECRET` | JWT密钥 | `your-secret-key` |
| `RUSTDESK_ID_SERVER` | ID服务器地址 | `192.168.1.66:21116` |
| `RUSTDESK_RELAY_SERVER` | Relay服务器地址 | `192.168.1.66:21117` |
| `RUSTDESK_API_SERVER` | API服务器地址 | `http://192.168.1.66:21114` |
| `RUSTDESK_KEY` | RustDesk公钥 | `key_content` |
| `MYSQL_HOST` | MySQL主机 | `localhost` |
| `MYSQL_PORT` | MySQL端口 | `3306` |
| `MYSQL_DATABASE` | 数据库名 | `rustdesk` |
| `MYSQL_USERNAME` | 数据库用户名 | `root` |
| `MYSQL_PASSWORD` | 数据库密码 | `password` |

## API 文档

### 主要端点

#### 认证相关
```
POST   /api/login              # 用户登录
POST   /api/logout             # 用户登出
GET    /api/login-options      # 获取登录选项
```

#### 用户管理
```
GET    /api/user/info          # 当前用户信息
POST   /api/user/changePassword # 修改密码
```

#### 设备管理
```
POST   /api/sysinfo            # 上报设备信息
GET    /api/peers              # 获取设备列表
```

#### 管理后台
```
POST   /api/admin/login        # 管理员登录
GET    /api/admin/user/list    # 用户列表
POST   /api/admin/user/create  # 创建用户
GET    /api/admin/group/list   # 群组列表
```

详细API文档请访问: http://localhost:21114/swagger-ui.html

## 数据库说明

项目使用 Flyway 进行数据库版本管理,包含15张表:

- `users` - 用户表
- `user_tokens` - Token表
- `peers` - 设备表
- `address_books` - 地址簿表
- `address_book_collection` - 地址簿集合
- `address_book_collection_rule` - 集合共享规则
- `tags` - 标签表
- `groups` - 群组表
- `oauth` - OAuth配置
- `user_third` - 第三方账号绑定
- `login_log` - 登录日志
- `audit_conn` - 连接审计
- `audit_file` - 文件审计
- `share_record` - 分享记录
- `server_cmd` - 服务器命令

## 开发状态

### ✅ 已完成
- 项目架构搭建
- 所有实体类创建 (15个)
- Repository层完成 (15个)
- Service层实现 (7个核心服务)
- Controller层基础实现 (6个控制器)
- 安全配置 (JWT + OAuth + BCrypt)
- 数据库迁移脚本
- API文档集成 (Swagger)
- 配置文件管理

### ⚠️ 待完善
- 时间字段类型统一 (部分实体需从LocalDateTime改为Long timestamp)
- LDAP集成完善
- OAuth完整流程测试
- 地址簿同步功能
- Web Client集成
- 单元测试编写
- Docker容器化部署

## 已知问题

1. **时间戳类型不一致**: 部分实体的时间字段(如ShareRecord.expire)需要从`LocalDateTime`改为`Long`以与Go版本保持一致
2. **LDAP依赖**: 默认已注释LDAP依赖,如需使用请在pom.xml中启用
3. **首次启动**: 需要清理旧的数据库文件: `rm -f data/rustdesk.db*`

## 修复建议

### 修复时间字段类型

需要将以下实体的时间字段改为Long类型(Unix timestamp毫秒):

```java
// ShareRecord.java
@Column(name = "expire")
private Long expire;  // 改为 Long

// 其他可能需要修改的时间字段类似处理
```

## 与Go版本对比

| 特性 | Go版本 | Java版本 | 说明 |
|------|--------|---------|------|
| 核心API | ✅ | ✅ | 完全兼容 |
| Web Admin | ✅ | ✅ | 功能一致 |
| OAuth | ✅ | ✅ | 支持GitHub/Google/OIDC |
| LDAP | ✅ | ⚠️ | 需启用依赖 |
| Web Client | ✅ | ⚠️ | 待集成 |
| Docker | ✅ | ⚠️ | 待完善 |
| 性能 | 高 | 中等 | JVM启动较慢 |

## 贡献

欢迎提交 Issue 和 Pull Request!

## 许可证

本项目参考 [lejianwen/rustdesk-api](https://github.com/lejianwen/rustdesk-api) 项目实现。

## 相关链接

- [RustDesk 官方](https://rustdesk.com/)
- [RustDesk API (Go版本)](https://github.com/lejianwen/rustdesk-api)
- [Spring Boot 文档](https://spring.io/projects/spring-boot)

---

**开发时间**: 2025年10月
**版本**: 2.0.0
**作者**: RustDesk API Team (Java Implementation)
