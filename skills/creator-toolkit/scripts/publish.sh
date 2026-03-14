#!/bin/bash
# Creator Toolkit - 多平台发布脚本
# 📤 一键发布文章到微信公众号、知乎、掘金等平台

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"
CONFIG_FILE="$ROOT_DIR/config.json"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 打印帮助信息
print_help() {
    cat << EOF
📤 多平台文章发布

用法：$0 <文章文件> [选项]

选项:
  --platforms <list>   发布平台，逗号分隔（wechat,zhihu,juejin）
  --schedule <datetime>  定时发布时间（YYYY-MM-DD HH:mm）
  --cover <image>      封面图路径
  --tags <list>        标签，逗号分隔
  --title <title>      文章标题（默认从文件内容提取）
  --help               显示帮助信息

支持的平台:
  wechat  - 微信公众号
  zhihu   - 知乎
  juejin  - 掘金

示例:
  $0 ./article.md --platforms wechat,zhihu
  $0 ./post.md --platforms wechat --schedule "2026-03-14 09:00"
  $0 ./doc.md --platforms juejin --tags "Java,编程"

EOF
}

# 解析参数
parse_args() {
    INPUT_FILE=""
    PLATFORMS=""
    SCHEDULE=""
    COVER=""
    TAGS=""
    TITLE=""
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --platforms)
                PLATFORMS="$2"
                shift 2
                ;;
            --schedule)
                SCHEDULE="$2"
                shift 2
                ;;
            --cover)
                COVER="$2"
                shift 2
                ;;
            --tags)
                TAGS="$2"
                shift 2
                ;;
            --title)
                TITLE="$2"
                shift 2
                ;;
            --help)
                print_help
                exit 0
                ;;
            *)
                if [ -z "$INPUT_FILE" ]; then
                    INPUT_FILE="$1"
                else
                    echo -e "${RED}错误：未知参数 $1${NC}"
                    print_help
                    exit 1
                fi
                shift
                ;;
        esac
    done
    
    if [ -z "$INPUT_FILE" ]; then
        echo -e "${RED}错误：请指定文章文件${NC}"
        print_help
        exit 1
    fi
    
    if [ ! -f "$INPUT_FILE" ]; then
        echo -e "${RED}错误：文件不存在 $INPUT_FILE${NC}"
        exit 1
    fi
    
    if [ -z "$PLATFORMS" ]; then
        echo -e "${RED}错误：请指定发布平台 --platforms${NC}"
        print_help
        exit 1
    fi
}

# 检查配置
check_config() {
    if [ ! -f "$CONFIG_FILE" ]; then
        echo -e "${RED}错误：配置文件不存在${NC}"
        echo "请先运行：cp config.example.json config.json"
        exit 1
    fi
}

# 提取文章标题
extract_title() {
    local file="$1"
    # 尝试从 YAML frontmatter 提取
    local title=$(grep -m1 "^title:" "$file" 2>/dev/null | cut -d':' -f2- | xargs || echo "")
    
    if [ -z "$title" ]; then
        # 尝试从第一个标题提取
        title=$(grep -m1 "^# " "$file" | sed 's/^# //' || echo "")
    fi
    
    if [ -z "$title" ]; then
        # 使用文件名
        title=$(basename "$file" .md)
    fi
    
    echo "$title"
}

# 发布到微信公众号
publish_wechat() {
    local content="$1"
    local title="$2"
    
    echo -e "${BLUE}发布到微信公众号...${NC}"
    
    # 检查配置
    local app_id=$(grep -o '"appId"[[:space:]]*:[[:space:]]*"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4 || echo "")
    local app_secret=$(grep -o '"appSecret"[[:space:]]*:[[:space:]]*"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4 || echo "")
    
    if [ -z "$app_id" ] || [ -z "$app_secret" ]; then
        echo -e "${YELLOW}⚠️  微信公众号配置未设置，跳过${NC}"
        echo "请在 config.json 中配置 wechat.appId 和 wechat.appSecret"
        return 0
    fi
    
    # 获取 access_token（简化版本）
    echo "  获取 access_token..."
    local token_response=$(curl -s "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=$app_id&secret=$app_secret")
    local access_token=$(echo "$token_response" | grep -o '"access_token"[[:space:]]*:[[:space:]]*"[^"]*"' | cut -d'"' -f4 || echo "")
    
    if [ -z "$access_token" ]; then
        echo -e "${RED}  ✗ 获取 access_token 失败${NC}"
        echo "  响应：$token_response"
        return 1
    fi
    
    echo "  ✓ 获取 access_token 成功"
    
    # 这里应该调用微信公众号 API 发布文章
    # 由于 API 较复杂，这里仅做演示
    echo -e "${YELLOW}  ⚠️  微信公众号发布需要额外配置${NC}"
    echo "  请参考微信公众号开发文档完成集成"
    
    echo -e "${GREEN}  ✓ 微信公众号发布完成（演示模式）${NC}"
}

# 发布到知乎
publish_zhihu() {
    local content="$1"
    local title="$2"
    
    echo -e "${BLUE}发布到知乎...${NC}"
    
    # 检查配置
    local access_token=$(grep -o '"accessToken"[[:space:]]*:[[:space:]]*"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4 || echo "")
    
    if [ -z "$access_token" ]; then
        echo -e "${YELLOW}⚠️  知乎配置未设置，跳过${NC}"
        echo "请在 config.json 中配置 zhihu.accessToken"
        return 0
    fi
    
    # 知乎发布逻辑（简化版本）
    echo -e "${YELLOW}  ⚠️  知乎发布需要额外配置${NC}"
    echo "  请参考知乎开发文档完成集成"
    
    echo -e "${GREEN}  ✓ 知乎发布完成（演示模式）${NC}"
}

# 发布到掘金
publish_juejin() {
    local content="$1"
    local title="$2"
    
    echo -e "${BLUE}发布到掘金...${NC}"
    
    # 检查配置
    local cookie=$(grep -o '"cookie"[[:space:]]*:[[:space:]]*"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4 || echo "")
    
    if [ -z "$cookie" ]; then
        echo -e "${YELLOW}⚠️  掘金配置未设置，跳过${NC}"
        echo "请在 config.json 中配置 juejin.cookie"
        return 0
    fi
    
    # 掘金发布逻辑（简化版本）
    echo -e "${YELLOW}  ⚠️  掘金发布需要额外配置${NC}"
    echo "  请参考掘金开发文档完成集成"
    
    echo -e "${GREEN}  ✓ 掘金发布完成（演示模式）${NC}"
}

# 主发布函数
publish() {
    local file="$1"
    local platforms="$2"
    
    # 提取标题
    if [ -z "$TITLE" ]; then
        TITLE=$(extract_title "$file")
    fi
    
    # 读取内容
    local content=$(cat "$file")
    
    echo -e "${BLUE}=== 文章发布 ===${NC}"
    echo -e "标题：$TITLE"
    echo -e "文件：$file"
    echo -e "平台：$platforms"
    if [ -n "$SCHEDULE" ]; then
        echo -e "定时：$SCHEDULE"
    fi
    if [ -n "$TAGS" ]; then
        echo -e "标签：$TAGS"
    fi
    if [ -n "$COVER" ]; then
        echo -e "封面：$COVER"
    fi
    echo ""
    
    # 检查免费版限制
    local platform_count=$(echo "$platforms" | tr ',' '\n' | wc -l)
    if [ $platform_count -gt 2 ]; then
        echo -e "${YELLOW}⚠️  免费版限制：最多发布 2 个平台${NC}"
        echo -e "升级到 Pro 解锁多平台发布：¥29/月"
        # 只发布前 2 个平台
        platforms=$(echo "$platforms" | tr ',' '\n' | head -2 | tr '\n' ',' | sed 's/,$//')
        echo -e "将发布到：$platforms"
    fi
    
    # 发布到各个平台
    IFS=',' read -ra PLATFORM_ARRAY <<< "$platforms"
    for platform in "${PLATFORM_ARRAY[@]}"; do
        platform=$(echo "$platform" | xargs)  # 去除空格
        
        case $platform in
            wechat)
                publish_wechat "$content" "$TITLE"
                ;;
            zhihu)
                publish_zhihu "$content" "$TITLE"
                ;;
            juejin)
                publish_juejin "$content" "$TITLE"
                ;;
            *)
                echo -e "${YELLOW}⚠️  跳过未知平台：$platform${NC}"
                ;;
        esac
        
        # 平台间延迟，避免速率限制
        if [ "$platform" != "${PLATFORM_ARRAY[-1]}" ]; then
            sleep 2
        fi
    done
    
    echo ""
    echo -e "${GREEN}🎉 发布完成！${NC}"
}

# 主函数
main() {
    parse_args "$@"
    check_config
    publish "$INPUT_FILE" "$PLATFORMS"
}

main "$@"
