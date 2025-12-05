@echo off
REM Ticket 标签管理系统 - 一键启动脚本 (Windows)
REM 使用方法: start.bat

chcp 65001 >nul
setlocal enabledelayedexpansion

echo ========================================
echo Ticket 标签管理系统 - 启动脚本
echo ========================================
echo.

REM 检查 Java 环境
echo [1/4] 检查 Java 环境...
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到 Java，请先安装 JDK 8 或更高版本
    pause
    exit /b 1
)
echo ✓ Java 环境检查通过
echo.

REM 检查 Maven 环境
echo [2/4] 检查 Maven 环境...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到 Maven，请先安装 Maven 3.6+
    pause
    exit /b 1
)
echo ✓ Maven 环境检查通过
echo.

REM 检查配置文件
echo [3/4] 检查配置文件...
if not exist "src\main\resources\application.properties" (
    echo 错误: 配置文件不存在
    pause
    exit /b 1
)
echo ✓ 配置文件检查完成
echo.

REM 编译项目
echo [4/4] 编译项目...
if not exist "target\classes" (
    echo 正在编译项目...
    call mvn clean compile -q
    if errorlevel 1 (
        echo 编译失败
        pause
        exit /b 1
    )
    echo ✓ 编译完成
) else (
    echo ✓ 项目已编译
)
echo.

REM 启动应用
echo ========================================
echo 正在启动应用...
echo ========================================
echo.
echo 应用将在以下地址启动:
echo   http://localhost:8080/tickets
echo.
echo 按 Ctrl+C 停止应用
echo.

REM 启动 Spring Boot 应用
call mvn spring-boot:run

pause

