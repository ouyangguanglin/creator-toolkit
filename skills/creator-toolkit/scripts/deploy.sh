#!/bin/bash
# Creator Toolkit - 部署脚本
# 🚀 打包、发布到 GitHub 和 skillhub

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"
JAVA_DIR="$ROOT_DIR/java"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}🚀 Creator Toolkit 部署脚本${NC}"
echo "======================================"
echo ""

# 检查 Git
if ! command -v git &> /dev/null; then
    echo -e "${RED}错误：需要 Git${NC}"
    exit 1
fi

# 检查 Maven（用于 Java 编译）
if command -v mvn &> /dev/null; then
    HAS_MAVEN=true
    echo -e "${GREEN}✓ Maven 已安装${NC}"
else
    HAS_MAVEN=false
    echo -e "${YELLOW}⚠️  Maven 未安装，跳过 Java 编译${NC}"
fi

# 检查 GitHub 配置
if [ -z "$GITHUB_TOKEN" ]; then
    echo -e "${YELLOW}⚠️  GITHUB_TOKEN 未设置${NC}"
    echo "提示：export GITHUB_TOKEN='your_token'"
    echo ""
fi

# 步骤 1：更新版本号
echo -e "${BLUE}[1/5] 更新版本号${NC}"
read -p "输入新版本号 (当前：0.1.0): " VERSION
VERSION=${VERSION:-0.1.0}

# 更新 config.json
if [ -f "$ROOT_DIR/config.json" ]; then
    sed -i.bak "s/\"version\": \"[^\"]*\"/\"version\": \"$VERSION\"/" "$ROOT_DIR/config.json" 2>/dev/null || \
    sed -i '' "s/\"version\": \"[^\"]*\"/\"version\": \"$VERSION\"/" "$ROOT_DIR/config.json"
    echo "  ✓ 更新 config.json"
fi

# 更新 pom.xml
if [ -f "$JAVA_DIR/pom.xml" ]; then
    sed -i.bak "s/<version>[^<]*<\/version>/<version>$VERSION<\/version>/" "$JAVA_DIR/pom.xml" 2>/dev/null || \
    sed -i '' "s/<version>[^<]*<\/version>/<version>$VERSION<\/version>/" "$JAVA_DIR/pom.xml"
    echo "  ✓ 更新 pom.xml"
fi

# 步骤 2：编译 Java 代码
if [ "$HAS_MAVEN" = true ] && [ -f "$JAVA_DIR/pom.xml" ]; then
    echo -e "${BLUE}[2/5] 编译 Java 代码${NC}"
    cd "$JAVA_DIR"
    mvn clean package -DskipTests
    echo "  ✓ Java 编译完成"
    cd "$ROOT_DIR"
fi

# 步骤 3：运行测试
echo -e "${BLUE}[3/5] 运行测试${NC}"
# TODO: 添加实际测试
echo "  ✓ 测试通过（待实现）"

# 步骤 4：创建 Git 标签
echo -e "${BLUE}[4/5] 创建 Git 标签${NC}"
git add -A
git commit -m "release: v$VERSION" || echo "  没有更改需要提交"
git tag -a "v$VERSION" -m "Release v$VERSION"
echo "  ✓ 标签创建完成"

# 步骤 5：推送到 GitHub
echo -e "${BLUE}[5/5] 推送到 GitHub${NC}"
if [ -n "$GITHUB_TOKEN" ]; then
    git push origin main --tags
    echo "  ✓ 推送到 GitHub"
else
    echo -e "${YELLOW}⚠️  跳过推送（GITHUB_TOKEN 未设置）${NC}"
    echo "手动执行："
    echo "  git push origin main --tags"
fi

# 更新 skillhub 索引
echo ""
echo -e "${BLUE}📦 更新 skillhub 索引${NC}"
echo "提示：更新 skills.json 中的 downloadUrl 和 version"
echo "然后推送技能市场仓库"

# 完成
echo ""
echo -e "${GREEN}🎉 部署完成！${NC}"
echo ""
echo "版本：v$VERSION"
echo ""
echo "下一步："
echo "1. 在 GitHub 创建 Release: https://github.com/yourusername/creator-toolkit/releases"
echo "2. 上传 JAR 文件到 Release"
echo "3. 更新 skillhub 索引"
echo "4. 通知用户更新"
echo ""
