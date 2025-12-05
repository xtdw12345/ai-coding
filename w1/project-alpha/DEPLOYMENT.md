# Ticket 标签管理系统 - 部署说明

## 一、环境要求

- **JDK**: JDK 8 或更高版本
- **Maven**: Maven 3.6+ 
- **MySQL**: MySQL 8.0+ 或 MariaDB 10.3+
- **操作系统**: Windows / Linux / macOS

## 二、数据库准备

### 1. 创建数据库

```sql
CREATE DATABASE ticket_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 执行初始化脚本

执行项目中的数据库初始化脚本：

```bash
mysql -u root -p ticket_system < docs/sql/init.sql
```

或者直接在 MySQL 客户端中执行 `docs/sql/init.sql` 文件中的 SQL 语句。

## 三、配置数据库连接

编辑 `src/main/resources/application.properties` 文件，修改数据库连接信息：

```properties
# 数据源配置
spring.datasource.url=jdbc:mysql://localhost:3306/ticket_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=your_username
spring.datasource.password=your_password
```

**注意**: 请将 `your_username` 和 `your_password` 替换为实际的数据库用户名和密码。

## 四、构建项目

### 方式一：使用 Maven 命令

```bash
# 进入项目目录
cd w1/project-alpha

# 清理并编译
mvn clean compile

# 打包（生成 JAR 文件）
mvn clean package
```

打包成功后，会在 `target` 目录下生成 `ticket-system-1.0.0.jar` 文件。

### 方式二：使用 IDE

在 IntelliJ IDEA 或 Eclipse 中：
1. 导入 Maven 项目
2. 等待依赖下载完成
3. 使用 IDE 的构建功能进行编译和打包

## 五、运行应用

### 方式一：JAR 方式运行（推荐）

```bash
# 直接运行 JAR 文件
java -jar target/ticket-system-1.0.0.jar

# 或者指定端口运行
java -jar target/ticket-system-1.0.0.jar --server.port=8080
```

### 方式二：Maven 方式运行

```bash
# 使用 Spring Boot Maven 插件运行
mvn spring-boot:run
```

### 方式三：IDE 中运行

1. 在 IDE 中找到 `TicketApplication.java` 主启动类
2. 右键选择 "Run" 或 "Debug"

## 六、访问应用

应用启动成功后，在浏览器中访问：

```
http://localhost:8080/tickets
```

默认端口为 8080，如果修改了端口，请使用相应的端口号。

## 七、打包为 WAR 部署到 Tomcat（可选）

如果需要部署到外部 Tomcat 服务器：

### 1. 修改 pom.xml

将 `<packaging>` 从 `jar` 改为 `war`：

```xml
<packaging>war</packaging>
```

### 2. 修改主启动类

让 `TicketApplication` 继承 `SpringBootServletInitializer`：

```java
@SpringBootApplication
@MapperScan("com.example.ticket.mapper")
public class TicketApplication extends SpringBootServletInitializer {
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TicketApplication.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(TicketApplication.class, args);
    }
}
```

### 3. 打包 WAR

```bash
mvn clean package
```

### 4. 部署到 Tomcat

将生成的 `target/ticket-system-1.0.0.war` 文件复制到 Tomcat 的 `webapps` 目录下，启动 Tomcat 即可。

**注意**: 由于使用了 JSP，建议使用 WAR 方式部署到 Tomcat，JAR 方式可能无法正常渲染 JSP 页面。

## 八、生产环境配置建议

### 1. 日志配置

在 `application.properties` 中配置日志级别：

```properties
# 生产环境建议使用 INFO 级别
logging.level.com.example.ticket=INFO
logging.level.org.springframework.web=WARN
logging.level.org.mybatis=WARN
```

### 2. 数据库连接池配置

可以添加连接池配置以优化性能：

```properties
# HikariCP 连接池配置（Spring Boot 默认使用）
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

### 3. 安全配置

- 确保数据库密码安全，不要提交到版本控制系统
- 使用环境变量或配置文件管理敏感信息
- 考虑添加 HTTPS 支持

## 九、常见问题

### 1. 数据库连接失败

- 检查数据库服务是否启动
- 检查数据库用户名和密码是否正确
- 检查数据库 URL 是否正确
- 检查防火墙设置

### 2. JSP 页面无法访问

- 确保 `tomcat-embed-jasper` 依赖已添加
- 检查 JSP 文件路径是否正确
- 如果使用 JAR 方式，建议改用 WAR 方式部署

### 3. 端口被占用

修改 `application.properties` 中的端口：

```properties
server.port=8081
```

### 4. 中文乱码

确保：
- 数据库字符集为 `utf8mb4`
- 应用编码为 `UTF-8`
- JSP 页面编码为 `UTF-8`

## 十、停止应用

- 如果使用 `java -jar` 运行，按 `Ctrl+C` 停止
- 如果使用 Maven 运行，按 `Ctrl+C` 停止
- 如果部署到 Tomcat，在 Tomcat 管理界面停止应用或停止 Tomcat 服务

## 十一、更新应用

1. 停止当前运行的应用
2. 重新构建项目：`mvn clean package`
3. 替换旧的 JAR/WAR 文件
4. 重新启动应用

---

**文档版本**: 1.0  
**最后更新**: 2024-12-05

