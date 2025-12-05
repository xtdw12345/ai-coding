Project Alpha - Ticket 标签管理系统实现计划
========================================

> 依据：`./specs/w1/0001-spec.md` 中的需求与设计  
> 代码目录：`./w1/project-alpha`

## 1. 开发总体策略

- **技术选型落地**
  - 使用 **JDK 8+**。
  - 使用 **Spring Boot 2.x/3.x** 作为后端框架。
  - 使用 **MyBatis** 作为持久层框架（MyBatis Spring Boot Starter）。
  - 使用 **JSP + JSTL** 作为前端视图技术。
  - 使用 **Maven** 作为构建工具。
- **分层结构**
  - `controller`：Spring MVC Controller，处理请求、参数解析、简单校验、调用 Service、返回视图或重定向。
  - `service`：Spring Service，业务逻辑、事务边界（使用 `@Transactional`）、组合 Mapper。
  - `mapper`：MyBatis Mapper 接口，定义 SQL 操作方法。
  - `model/domain`：`Ticket`, `Tag`, `TicketTag` 等实体类。
- **迭代顺序**
  1. 项目骨架 & Spring Boot 项目初始化。
  2. 数据库建表 & Spring Boot 数据源配置。
  3. MyBatis Mapper 实现与单元级验证。
  4. Service 层实现 Ticket/Tag 核心用例。
  5. Controller + JSP 页面实现最小可用的"列表 + 新建"。
  6. 增量补齐编辑/删除/完成/取消完成/搜索/标签筛选等功能。
  7. UI 优化、表单校验、错误处理与简单安全加固。

## 2. 项目结构规划（目录与包）

在 `./w1/project-alpha` 下创建 Spring Boot Maven 工程，主要结构如下：

```
w1/project-alpha/
├── pom.xml                          # Maven 配置（Spring Boot、MyBatis、MySQL 等依赖）
├── src/main/java
│   └── com/example/ticket           # 根包（可按需调整）
│       ├── TicketApplication.java   # Spring Boot 主启动类
│       ├── controller
│       │   ├── TicketController     # Spring MVC Controller
│       │   └── TagController        # 标签相关 Controller（可选）
│       ├── service
│       │   ├── TicketService        # Service 接口
│       │   ├── TicketServiceImpl    # Service 实现类（使用 @Service）
│       │   ├── TagService
│       │   └── TagServiceImpl
│       ├── mapper                    # MyBatis Mapper 接口
│       │   ├── TicketMapper
│       │   ├── TagMapper
│       │   └── TicketTagMapper
│       ├── model                     # 实体类
│       │   ├── Ticket
│       │   ├── Tag
│       │   └── TicketTag（可选）
│       └── util                      # 工具类
│           ├── PageResult            # 分页结果封装
│           └── 其他工具类
├── src/main/resources
│   ├── application.properties       # Spring Boot 配置文件（数据源、MyBatis 等）
│   ├── mapper/                       # MyBatis XML 映射文件
│   │   ├── TicketMapper.xml
│   │   ├── TagMapper.xml
│   │   └── TicketTagMapper.xml
│   └── static                        # 静态资源（CSS、JS）
│       ├── css/
│       │   └── main.css
│       └── js/
│           └── main.js
└── src/main/webapp                   # JSP 页面（Spring Boot 需要特殊配置）
    └── WEB-INF/views/
        ├── ticket-list.jsp
        └── ticket-form.jsp
```

## 3. 数据库实现步骤

### 3.1 建库建表

1. 在 MySQL 中创建数据库（例如 `ticket_system`）：
   - 字符集：`utf8mb4`。
2. 按 `0001-spec` 设计建立三张表：
   - `ticket`
   - `tag`
   - `ticket_tag`
3. 编写初始化 SQL 脚本放在 `docs/sql/init.sql`（可选），便于重建环境：
   - 建库 + 建表 + 必要索引 + 少量示例数据（1~2 条 ticket 与标签）。

### 3.2 Spring Boot 数据源与 MyBatis 配置

- 在 `src/main/resources/application.properties` 中配置：
  ```properties
  # 数据源配置
  spring.datasource.url=jdbc:mysql://localhost:3306/ticket_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  spring.datasource.username=root
  spring.datasource.password=your_password
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  
  # MyBatis 配置
  mybatis.mapper-locations=classpath:mapper/*.xml
  mybatis.type-aliases-package=com.example.ticket.model
  mybatis.configuration.map-underscore-to-camel-case=true
  
  # JSP 视图配置（Spring Boot 需要特殊配置）
  spring.mvc.view.prefix=/WEB-INF/views/
  spring.mvc.view.suffix=.jsp
  
  # 应用配置
  server.port=8080
  spring.application.name=ticket-system
  ```
- **注意**：Spring Boot 默认不支持 JSP，需要：
  - 添加 `spring-boot-starter-tomcat`（provided scope）和 `tomcat-embed-jasper` 依赖。
  - 或者使用内嵌 Tomcat 并配置 JSP 支持。

## 4. MyBatis Mapper 层实现计划

### 4.1 实体类设计

- `Ticket`
  - 字段：`id`, `title`, `description`, `status`, `createdAt`, `updatedAt`, `completedAt`
  - 使用 `@Data` 或 Lombok 注解（可选）。
  - 可选：`List<Tag> tags`（用于视图展示时携带标签列表）。
- `Tag`
  - 字段：`id`, `name`, `createdAt`
- `TicketTag`
  - 字段：`ticketId`, `tagId`（如不需要单独对象，可省略）。

### 4.2 TicketMapper

- **Mapper 接口**（`TicketMapper.java`）：
  ```java
  @Mapper
  public interface TicketMapper {
      Long insert(Ticket ticket);
      int update(Ticket ticket);
      int deleteById(Long id);
      Ticket findById(Long id);
      List<Ticket> findPage(@Param("keyword") String keyword, 
                           @Param("tagId") Long tagId, 
                           @Param("offset") int offset, 
                           @Param("limit") int limit);
      int count(@Param("keyword") String keyword, @Param("tagId") Long tagId);
      int updateStatus(@Param("id") Long id, 
                      @Param("status") int status, 
                      @Param("completedAt") Timestamp completedAt);
  }
  ```
- **XML 映射文件**（`TicketMapper.xml`）：
  - 在 `src/main/resources/mapper/TicketMapper.xml` 中编写 SQL。
  - 使用 MyBatis 的 `<select>`, `<insert>`, `<update>`, `<delete>` 标签。
  - 使用 `#{}` 参数绑定防止 SQL 注入。
  - 使用 `<resultMap>` 映射结果集到实体类。

### 4.3 TagMapper

- **Mapper 接口**：
  ```java
  @Mapper
  public interface TagMapper {
      Tag findByName(String name);
      Tag findById(Long id);
      Long insert(Tag tag);
      List<Tag> findAll();
      List<Tag> findByTicketId(Long ticketId);
  }
  ```
- **XML 映射文件**：在 `TagMapper.xml` 中实现对应 SQL。

### 4.4 TicketTagMapper

- **Mapper 接口**：
  ```java
  @Mapper
  public interface TicketTagMapper {
      int insert(@Param("ticketId") Long ticketId, @Param("tagId") Long tagId);
      int deleteByTicketId(Long ticketId);
      List<Long> findTagIdsByTicketId(Long ticketId);
      List<Long> findTicketIdsByTagId(Long tagId);
  }
  ```
- **XML 映射文件**：在 `TicketTagMapper.xml` 中实现对应 SQL。

## 5. Service 层实现计划

### 5.1 TicketService

- **Service 接口**：
  ```java
  public interface TicketService {
      Long createTicket(String title, String description, List<String> tagNames);
      void updateTicket(Long id, String title, String description, List<String> tagNames);
      void deleteTicket(Long id);
      void completeTicket(Long id);
      void reopenTicket(Long id);
      Ticket getTicketWithTags(Long id);
      PageResult<Ticket> listTickets(String keyword, Long tagId, int page, int pageSize);
  }
  ```
- **Service 实现类**（`TicketServiceImpl`）：
  - 使用 `@Service` 注解标记为 Spring Bean。
  - 注入 `TicketMapper`, `TagMapper`, `TicketTagMapper`（使用 `@Autowired` 或构造器注入）。
  - 注入 `TagService` 用于标签处理。
  - **事务处理**：
    - 在需要事务的方法上使用 `@Transactional` 注解。
    - Spring Boot 会自动管理事务，无需手动处理 Connection。
  - **创建/更新时的标签处理**：
    - 调用 `TagService.findOrCreateTagsByNames(tagNames)` 获取标签 ID 列表。
    - 使用 `TicketTagMapper` 维护关联（插入或重建）。
    - 更新时先删除旧关联，再插入新关联。

### 5.2 TagService

- **Service 接口**：
  ```java
  public interface TagService {
      List<Tag> listAllTags();
      List<Tag> findOrCreateTagsByNames(List<String> names);
  }
  ```
- **Service 实现类**（`TagServiceImpl`）：
  - 使用 `@Service` 注解。
  - 注入 `TagMapper`。
  - **实现要点**：
    - `findOrCreateTagsByNames`：
      - 去重、去掉空白字符串。
      - 对每个 tagName：
        - 先 `TagMapper.findByName`，若存在则复用；
        - 不存在则 `TagMapper.insert` 新记录并返回。
      - 使用 `@Transactional` 确保标签创建的原子性。

## 6. Controller 层与路由实现计划

### 6.1 Spring MVC Controller 配置

- **无需 web.xml**：Spring Boot 使用 Java 配置或自动配置。
- **编码配置**：在 `application.properties` 中配置：
  ```properties
  server.servlet.encoding.charset=UTF-8
  server.servlet.encoding.enabled=true
  server.servlet.encoding.force=true
  ```
- **路由映射**：使用 Spring MVC 的 `@Controller` 和 `@RequestMapping` 注解。

### 6.2 TicketController 具体实现步骤

- **Controller 类**：
  ```java
  @Controller
  @RequestMapping("/tickets")
  public class TicketController {
      @Autowired
      private TicketService ticketService;
      
      @Autowired
      private TagService tagService;
      
      // 列表接口
      @GetMapping
      public String list(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) Long tagId,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size,
                        Model model) {
          PageResult<Ticket> pageResult = ticketService.listTickets(keyword, tagId, page, size);
          List<Tag> allTags = tagService.listAllTags();
          model.addAttribute("tickets", pageResult.getData());
          model.addAttribute("pageInfo", pageResult);
          model.addAttribute("allTags", allTags);
          model.addAttribute("currentTagId", tagId);
          model.addAttribute("keyword", keyword);
          return "ticket-list";
      }
      
      // 新建页面
      @GetMapping("/new")
      public String newForm(Model model) {
          List<Tag> allTags = tagService.listAllTags();
          model.addAttribute("allTags", allTags);
          return "ticket-form";
      }
      
      // 创建接口
      @PostMapping
      public String create(@RequestParam String title,
                          @RequestParam(required = false) String description,
                          @RequestParam(required = false) List<String> tags,
                          RedirectAttributes redirectAttributes) {
          try {
              ticketService.createTicket(title, description, tags);
              redirectAttributes.addFlashAttribute("message", "创建成功");
              return "redirect:/tickets";
          } catch (Exception e) {
              redirectAttributes.addFlashAttribute("error", e.getMessage());
              return "redirect:/tickets/new";
          }
      }
      
      // 编辑页面
      @GetMapping("/{id}/edit")
      public String editForm(@PathVariable Long id, Model model) {
          Ticket ticket = ticketService.getTicketWithTags(id);
          List<Tag> allTags = tagService.listAllTags();
          model.addAttribute("ticket", ticket);
          model.addAttribute("allTags", allTags);
          return "ticket-form";
      }
      
      // 更新接口
      @PostMapping("/{id}/update")
      public String update(@PathVariable Long id,
                          @RequestParam String title,
                          @RequestParam(required = false) String description,
                          @RequestParam(required = false) List<String> tags,
                          RedirectAttributes redirectAttributes) {
          try {
              ticketService.updateTicket(id, title, description, tags);
              redirectAttributes.addFlashAttribute("message", "更新成功");
              return "redirect:/tickets";
          } catch (Exception e) {
              redirectAttributes.addFlashAttribute("error", e.getMessage());
              return "redirect:/tickets/" + id + "/edit";
          }
      }
      
      // 删除接口
      @PostMapping("/{id}/delete")
      public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
          ticketService.deleteTicket(id);
          redirectAttributes.addFlashAttribute("message", "删除成功");
          return "redirect:/tickets";
      }
      
      // 完成接口
      @PostMapping("/{id}/complete")
      public String complete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
          ticketService.completeTicket(id);
          redirectAttributes.addFlashAttribute("message", "标记完成");
          return "redirect:/tickets";
      }
      
      // 取消完成接口
      @PostMapping("/{id}/reopen")
      public String reopen(@PathVariable Long id, RedirectAttributes redirectAttributes) {
          ticketService.reopenTicket(id);
          redirectAttributes.addFlashAttribute("message", "取消完成");
          return "redirect:/tickets";
      }
  }
  ```

## 7. JSP 页面与前端实现计划

### 7.1 `ticket-list.jsp`

- **布局**：
  - 顶部：标题“Ticket 列表”、`新建 Ticket` 按钮。
  - 筛选区：
    - 搜索输入框 + “搜索”按钮（提交到 `/tickets`）。
    - 标签筛选下拉 / 标签云（点击提交带 `tagId` 参数的请求）。
  - 列表表格：
    - 列：标题、状态、标签、创建时间、更新时间、操作。
    - 状态为“已完成”的行使用灰色/删除线样式。
  - 底部：分页组件（上一页/下一页/页码）。
- **交互**：
  - 删除按钮：`onclick="return confirm('确认要删除该 Ticket 吗？此操作不可恢复');"`。
  - 完成/取消完成按钮：普通表单提交或 AJAX（首版可用表单）。

### 7.2 `ticket-form.jsp`

- **表单字段**：
  - 标题（`<input type="text">`）。
  - 描述（`<textarea>`）。
  - 标签：
    - 已有标签列表（checkbox 或 multi-select）。
    - 新标签输入框（支持逗号分隔多个标签，提交到后端统一处理）。
- **前端校验**（在 `main.js` 中实现）：
  - 提交前校验标题非空、长度限制。
  - 校验失败时阻止提交，并在表单附近显示错误消息。
- **复用**：
  - 通过判断是否有 `ticket.id`，决定是“创建模式”还是“编辑模式”（按钮文字、表单 action 不同）。

### 7.3 CSS 与基础 UI 规范

- 在 `main.css` 中定义基础样式：
  - 整体布局宽度、字体、按钮风格。
  - Ticket 状态样式（未完成普通，已完成灰色+删除线）。
  - 标签样式（如蓝色小圆角块）。

## 8. 校验、错误处理与安全

- **服务端校验**：
  - Service 层统一校验标题非空、长度限制。
  - 若校验失败，抛业务异常（如 `IllegalArgumentException`），在 Controller 捕获并反馈到 JSP。
  - 可使用 Spring 的 `@Valid` 和 Bean Validation（JSR-303）进行参数校验。
- **SQL 注入防护**：
  - MyBatis 使用 `#{}` 参数绑定自动防止 SQL 注入（底层使用 PreparedStatement）。
  - 避免使用 `${}` 进行字符串拼接。
- **XSS 防护**：
  - JSP 输出用户输入的内容时使用 JSTL/EL 的默认 HTML 转义（`<c:out>` 标签）。
  - Spring Boot 默认开启 XSS 防护（可通过配置调整）。
- **统一错误处理**：
  - 使用 `@ControllerAdvice` 创建全局异常处理器。
  - 配置自定义错误页面（`src/main/resources/templates/error.html` 或 JSP）。

## 9. 迭代里程碑与任务拆分

### 9.1 里程碑 1：项目骨架 & 基础环境

- 使用 Spring Initializr 或手动创建 Spring Boot Maven 项目到 `w1/project-alpha`。
- 配置 `pom.xml` 依赖：
  - `spring-boot-starter-web`（Web 支持）
  - `spring-boot-starter-jdbc` 或 `mybatis-spring-boot-starter`（MyBatis）
  - `mysql-connector-java`（MySQL 驱动）
  - `tomcat-embed-jasper` 和 `jstl`（JSP 支持）
  - `spring-boot-starter-test`（测试）
- 创建主启动类 `TicketApplication.java`（使用 `@SpringBootApplication`）。
- 配置 `application.properties`（数据源、MyBatis、JSP 视图）。
- 确认 Spring Boot 应用可以启动。

### 9.2 里程碑 2：数据库与 MyBatis Mapper

- 创建 MySQL 数据库及三张表（执行 SQL 脚本）。
- 创建实体类：`Ticket`, `Tag`, `TicketTag`。
- 创建 MyBatis Mapper 接口：`TicketMapper`, `TagMapper`, `TicketTagMapper`。
- 创建对应的 XML 映射文件，实现基础 CRUD SQL。
- 在主启动类上添加 `@MapperScan("com.example.ticket.mapper")` 扫描 Mapper。
- 通过 JUnit 测试或简单 `main` 方法验证 Mapper 方法正常。

### 9.3 里程碑 3：Service 层 + 基本用例

- 实现 `TicketService` / `TagService` 接口及其实现类。
- 使用 `@Service` 和 `@Transactional` 注解。
- 实现 Ticket 创建/编辑时的标签逻辑与事务。
- 创建 `PageResult` 工具类用于分页结果封装。
- 编写若干 Service 层测试（使用 `@SpringBootTest`）。

### 9.4 里程碑 4：最小可用 UI（MVP）

- 实现 `TicketController`，配置路由映射。
- 实现 `ticket-list.jsp` 与 `ticket-form.jsp`。
- 实现列表展示 + 新建 Ticket（含标签创建）。
- 完成基本前后端联通，能通过浏览器完成一次"新建并查看 Ticket"的完整流程。

### 9.5 里程碑 5：功能完善

- 实现编辑、删除、完成/取消完成接口与前端按钮。
- 实现按标题搜索与按标签筛选。
- 实现分页功能。
- 添加全局异常处理（`@ControllerAdvice`）。

### 9.6 里程碑 6：优化与清理

- 补充前端表单校验、删除确认。
- 整理样式与页面布局，提升易用性。
- 添加必要的日志输出（使用 SLF4J + Logback，Spring Boot 默认集成）。
- 编写简单的部署说明（Spring Boot 可打包为 JAR 直接运行，或打包为 WAR 部署到 Tomcat）。

## 10. 后续扩展预留

- 在 Service/DAO 层接口设计中预留可扩展字段（如优先级、截止日期）。
- 通过统一的 `BaseDao` 或 `AbstractService` 抽取通用逻辑，为未来扩展（例如标签管理页面、统计报表）留接口。

## 11. Maven 依赖配置（pom.xml 示例）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.18</version>
        <relativePath/>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>ticket-system</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <java.version>8</java.version>
        <mybatis.version>2.3.1</mybatis.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Web Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- MyBatis Spring Boot Starter -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis.version}</version>
        </dependency>
        
        <!-- MySQL Driver -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- JSP Support -->
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <scope>provided</scope>
        </dependency>
        
        <!-- JSTL -->
        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>
            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
        </dependency>
        
        <!-- Lombok (可选，简化实体类) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Spring Boot Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## 12. Spring Boot 主启动类示例

```java
package com.example.ticket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.ticket.mapper")
public class TicketApplication {
    public static void main(String[] args) {
        SpringApplication.run(TicketApplication.class, args);
    }
}
```

## 13. 注意事项

1. **JSP 支持**：Spring Boot 默认不支持 JSP，需要：
   - 添加 `tomcat-embed-jasper` 依赖。
   - 配置视图前缀和后缀（`spring.mvc.view.prefix` 和 `spring.mvc.view.suffix`）。
   - 如果打包为 JAR，JSP 可能无法正常工作，建议打包为 WAR 或使用 Thymeleaf 替代。

2. **MyBatis 配置**：
   - 确保 `@MapperScan` 扫描到所有 Mapper 接口。
   - XML 映射文件路径需与 `mybatis.mapper-locations` 配置一致。
   - 启用驼峰命名转换：`mybatis.configuration.map-underscore-to-camel-case=true`。

3. **事务管理**：
   - Spring Boot 自动配置事务管理器，只需在 Service 方法上使用 `@Transactional`。

4. **打包部署**：
   - JAR 方式：`mvn clean package`，然后 `java -jar target/ticket-system-1.0.0.jar`。
   - WAR 方式：修改 `pom.xml` 的 `<packaging>` 为 `war`，并实现 `SpringBootServletInitializer`。

以上实现计划将指导后续在 `./w1/project-alpha` 中逐步完成代码开发，可按里程碑顺序逐步推进，每个阶段都可独立验证与回滚。
