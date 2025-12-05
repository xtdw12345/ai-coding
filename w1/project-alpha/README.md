# Ticket 标签管理系统

## 项目简介

Ticket 标签管理系统是一个基于 Spring Boot + MyBatis + MySQL + JSP 的简单待办事项管理系统，支持通过标签对 Ticket 进行分类与筛选。

## 技术栈

- **后端框架**: Spring Boot 2.7.18
- **持久层**: MyBatis 2.3.1
- **数据库**: MySQL 8.0+
- **前端视图**: JSP + JSTL
- **构建工具**: Maven
- **JDK版本**: JDK 8+

## 项目结构

```
w1/project-alpha/
├── pom.xml                          # Maven 配置文件
├── start.sh                         # 一键启动脚本 (macOS/Linux)
├── start.bat                        # 一键启动脚本 (Windows)
├── start-jar.sh                     # JAR 方式启动脚本 (macOS/Linux)
├── README.md                        # 项目说明文档
├── DEPLOYMENT.md                    # 部署说明文档
├── src/main/java
│   └── com/example/ticket           # 主包
│       ├── TicketApplication.java   # Spring Boot 主启动类
│       ├── controller               # Controller 层
│       ├── service                  # Service 层
│       ├── mapper                   # MyBatis Mapper 接口
│       ├── model                    # 实体类
│       └── util                     # 工具类
├── src/main/resources
│   ├── application.properties       # Spring Boot 配置文件
│   ├── mapper/                      # MyBatis XML 映射文件
│   └── static/                      # 静态资源（CSS、JS）
├── src/main/webapp                  # JSP 页面
│   └── WEB-INF/views/
└── docs/
    └── sql/
        └── init.sql                 # 数据库初始化脚本
```

## 环境要求

- JDK 8 或更高版本
- Maven 3.6+
- MySQL 8.0+
- IDE（推荐 IntelliJ IDEA 或 Eclipse）

## 快速开始

### 1. 数据库准备

创建数据库并执行初始化脚本：

```sql
CREATE DATABASE ticket_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 配置数据库连接

编辑 `src/main/resources/application.properties`，修改数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ticket_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. 编译和运行

#### 方式一：使用一键启动脚本（推荐）

**macOS/Linux:**
```bash
./start.sh
```

**Windows:**
```cmd
start.bat
```

#### 方式二：使用 Maven 命令

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/ticket-system-1.0.0.jar
```

#### 方式三：使用 JAR 启动脚本（macOS/Linux）

```bash
./start-jar.sh
```

### 4. 访问应用

启动成功后，访问：http://localhost:8080

## 开发计划

- [x] 里程碑 1：项目骨架 & 基础环境
- [x] 里程碑 2：数据库与 MyBatis Mapper
- [x] 里程碑 3：Service 层 + 基本用例
- [x] 里程碑 4：最小可用 UI（MVP）
- [x] 里程碑 5：功能完善
- [x] 里程碑 6：优化与清理

**项目状态**: ✅ 已完成所有里程碑

## 功能特性

### 已实现功能

- ✅ **Ticket 管理**
  - 创建、编辑、删除 Ticket
  - 标记完成/取消完成
  - 按标题搜索
  - 按标签筛选
  - 分页显示

- ✅ **标签管理**
  - 自动创建标签
  - 为 Ticket 添加/删除标签
  - 标签列表展示

- ✅ **用户体验**
  - 响应式设计（支持移动端）
  - 表单验证（前端+后端）
  - 删除确认
  - 消息提示
  - 友好的错误处理

- ✅ **技术特性**
  - Spring Boot + MyBatis + MySQL
  - JSP + JSTL 视图
  - 事务管理
  - 全局异常处理
  - 日志记录

## 部署说明

详细的部署说明请参考 [DEPLOYMENT.md](DEPLOYMENT.md)

## 许可证

本项目仅供学习和参考使用。

