#!/bin/bash
# CreatorToolkit 构建脚本

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo "🦞 开始构建 CreatorToolkit..."
echo "项目目录：$PROJECT_DIR"

cd "$PROJECT_DIR"

# 检查 Java 版本
echo "检查 Java 版本..."
java -version

# 检查 Maven
echo "检查 Maven..."
mvn -version

# 清理并构建
echo "清理旧构建..."
mvn clean

echo "编译项目..."
mvn compile

echo "打包..."
mvn package -DskipTests

echo ""
echo "✅ 构建完成！"
echo "可执行文件：target/creator-toolkit-1.0.0-SNAPSHOT.jar"
echo ""
echo "运行方式:"
echo "  java -jar target/creator-toolkit-1.0.0-SNAPSHOT.jar"
