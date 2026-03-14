#!/bin/bash
# Creator Toolkit - 主入口脚本
# 🧰 个人创作者的效率工具箱

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"
CONFIG_FILE="$ROOT_DIR/config.json"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印帮助信息
print_help() {
    cat << EOF
🧰 Creator Toolkit - 个人创作者的效率工具箱

用法：$0 <命令> [选项]

命令:
  compress    批量压缩图片
  watermark   批量添加水印
  convert     格式转换
  publish     发布文章到多平台
  status      查看配置状态
  help        显示帮助信息

示例:
  $0 compress ./images --quality 80
  $0 publish ./article.md --platforms wechat,zhihu
  $0 convert ./article.md --target wechat

使用 "$0 help <命令>" 查看具体命令的帮助信息。

EOF
}

# 检查 Java 环境
check_java() {
    if ! command -v java &> /dev/null; then
        echo -e "${RED}错误：需要 Java 环境${NC}"
        echo "请先安装 Java 17+: https://adoptium.net/"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        echo -e "${YELLOW}警告：建议升级到 Java 17+${NC}"
    fi
}

# 检查配置文件
check_config() {
    if [ ! -f "$CONFIG_FILE" ]; then
        echo -e "${YELLOW}配置文件不存在，创建默认配置...${NC}"
        cp "$ROOT_DIR/config.example.json" "$CONFIG_FILE" 2>/dev/null || true
        echo -e "请编辑 ${BLUE}$CONFIG_FILE${NC} 填写你的配置"
    fi
}

# 主函数
main() {
    if [ $# -eq 0 ]; then
        print_help
        exit 0
    fi
    
    COMMAND=$1
    shift
    
    case "$COMMAND" in
        compress)
            exec "$SCRIPT_DIR/compress.sh" "$@"
            ;;
        watermark)
            exec "$SCRIPT_DIR/watermark.sh" "$@"
            ;;
        convert)
            exec "$SCRIPT_DIR/convert.sh" "$@"
            ;;
        publish)
            exec "$SCRIPT_DIR/publish.sh" "$@"
            ;;
        status)
            check_java
            check_config
            echo -e "${GREEN}✓ 环境检查通过${NC}"
            ;;
        help|--help|-h)
            if [ $# -gt 0 ]; then
                exec "$SCRIPT_DIR/$1.sh" --help
            else
                print_help
            fi
            ;;
        *)
            echo -e "${RED}未知命令：$COMMAND${NC}"
            print_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
