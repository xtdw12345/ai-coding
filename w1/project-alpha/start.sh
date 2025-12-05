#!/bin/bash

# Ticket 标签管理系统 - 一键启动脚本 (macOS/Linux)
# 使用方法: ./start.sh

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 项目目录
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Ticket 标签管理系统 - 启动脚本${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 检查 Java 环境
echo -e "${YELLOW}[1/4] 检查 Java 环境...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}错误: 未找到 Java，请先安装 JDK 8 或更高版本${NC}"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 8 ]; then
    echo -e "${RED}错误: Java 版本过低，需要 JDK 8 或更高版本${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Java 版本: $(java -version 2>&1 | head -n 1)${NC}"

# 检查 Maven 环境
echo -e "${YELLOW}[2/4] 检查 Maven 环境...${NC}"
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}错误: 未找到 Maven，请先安装 Maven 3.6+${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Maven 版本: $(mvn -version | head -n 1)${NC}"

# 检查配置文件
echo -e "${YELLOW}[3/4] 检查配置文件...${NC}"
CONFIG_FILE="src/main/resources/application.properties"
if [ ! -f "$CONFIG_FILE" ]; then
    echo -e "${RED}错误: 配置文件不存在: $CONFIG_FILE${NC}"
    exit 1
fi

# 检查数据库配置
DB_PASSWORD=$(grep "spring.datasource.password" "$CONFIG_FILE" | cut -d'=' -f2)
if [ "$DB_PASSWORD" = "your_password" ]; then
    echo -e "${YELLOW}⚠ 警告: 请先配置数据库连接信息 (application.properties)${NC}"
    echo -e "${YELLOW}   当前密码配置为默认值 'your_password'${NC}"
    read -p "是否继续启动? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo -e "${GREEN}✓ 配置文件检查完成${NC}"

# 编译项目（如果需要）
echo -e "${YELLOW}[4/4] 编译项目...${NC}"
if [ ! -d "target" ] || [ "target/classes" -ot "pom.xml" ]; then
    echo "正在编译项目..."
    mvn clean compile -q
    echo -e "${GREEN}✓ 编译完成${NC}"
else
    echo -e "${GREEN}✓ 项目已编译${NC}"
fi

# 启动应用
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}正在启动应用...${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "应用将在以下地址启动:"
echo -e "  ${YELLOW}http://localhost:8080/tickets${NC}"
echo ""
echo -e "按 ${YELLOW}Ctrl+C${NC} 停止应用"
echo ""

# 启动 Spring Boot 应用
mvn spring-boot:run

