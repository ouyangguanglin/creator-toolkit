#!/bin/bash
# Creator Toolkit - 文章格式转换脚本
# 📝 Markdown → 微信公众号/知乎/HTML 格式

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 打印帮助信息
print_help() {
    cat << EOF
📝 文章格式转换

用法：$0 <输入文件> [选项]

选项:
  --target <platform>  目标平台（wechat|zhihu|html）
  --output <file>      输出文件路径
  --extract-cover      自动提取封面图（第一张图片）
  --help               显示帮助信息

支持的目标平台:
  wechat  - 微信公众号（带样式 HTML）
  zhihu   - 知乎（Markdown 优化）
  html    - 标准 HTML

示例:
  $0 ./article.md --target wechat
  $0 ./post.md --target html --output ./output.html
  $0 ./doc.md --target wechat --extract-cover

EOF
}

# 解析参数
parse_args() {
    INPUT_FILE=""
    TARGET=""
    OUTPUT_FILE=""
    EXTRACT_COVER=false
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --target)
                TARGET="$2"
                shift 2
                ;;
            --output)
                OUTPUT_FILE="$2"
                shift 2
                ;;
            --extract-cover)
                EXTRACT_COVER=true
                shift
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
        echo -e "${RED}错误：请指定输入文件${NC}"
        print_help
        exit 1
    fi
    
    if [ ! -f "$INPUT_FILE" ]; then
        echo -e "${RED}错误：文件不存在 $INPUT_FILE${NC}"
        exit 1
    fi
    
    if [ -z "$TARGET" ]; then
        TARGET="html"
    fi
}

# 提取封面图
extract_cover() {
    local file="$1"
    local cover=$(grep -oE '!\\[.*\\]\\(.*\\)' "$file" | head -1 | grep -oE 'http[^)]+' || echo "")
    if [ -n "$cover" ]; then
        echo "$cover"
    fi
}

# 转换为微信公众号格式
convert_to_wechat() {
    local input="$1"
    local output="$2"
    
    echo -e "${BLUE}转换为微信公众号格式...${NC}"
    
    # 读取 Markdown 内容
    local content=$(cat "$input")
    
    # 创建 HTML 模板（微信公众号风格）
    cat > "$output" << 'HTML_HEAD'
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            line-height: 1.75;
            color: #333;
            max-width: 677px;
            margin: 0 auto;
            padding: 20px;
        }
        h1, h2, h3, h4, h5, h6 {
            color: #1a1a1a;
            margin-top: 24px;
            margin-bottom: 16px;
            font-weight: 600;
        }
        h1 { font-size: 24px; border-bottom: 1px solid #eaecef; padding-bottom: 0.3em; }
        h2 { font-size: 20px; border-bottom: 1px solid #eaecef; padding-bottom: 0.3em; }
        h3 { font-size: 16px; }
        p { margin: 16px 0; }
        code {
            background-color: #f6f8fa;
            padding: 0.2em 0.4em;
            border-radius: 3px;
            font-family: SFMono-Regular, Consolas, "Liberation Mono", Menlo, monospace;
            font-size: 85%;
        }
        pre {
            background-color: #f6f8fa;
            padding: 16px;
            border-radius: 6px;
            overflow: auto;
        }
        pre code {
            background: none;
            padding: 0;
        }
        blockquote {
            border-left: 4px solid #dfe2e5;
            padding-left: 16px;
            color: #6a737d;
            margin: 16px 0;
        }
        img {
            max-width: 100%;
            height: auto;
            display: block;
            margin: 16px auto;
        }
        a { color: #0366d6; text-decoration: none; }
        a:hover { text-decoration: underline; }
        ul, ol { padding-left: 2em; }
        hr {
            height: 0.25em;
            padding: 0;
            margin: 24px 0;
            background-color: #e1e4e8;
            border: 0;
        }
        .wechat-title {
            font-size: 22px;
            font-weight: bold;
            color: #000;
            text-align: center;
            margin-bottom: 24px;
        }
    </style>
</head>
<body>
HTML_HEAD

    # 简单的 Markdown 转 HTML（基础版本）
    # 实际生产环境应该使用专门的 Markdown 解析器
    local body_content="$content"
    
    # 标题转换
    body_content=$(echo "$body_content" | sed 's/^# \(.*\)/<h1>\1<\/h1>/g')
    body_content=$(echo "$body_content" | sed 's/^## \(.*\)/<h2>\1<\/h2>/g')
    body_content=$(echo "$body_content" | sed 's/^### \(.*\)/<h3>\1<\/h3>/g')
    
    # 代码块转换（简单处理）
    body_content=$(echo "$body_content" | sed 's/```\([^`]*\)```/<pre><code>\1<\/code><\/pre>/g')
    body_content=$(echo "$body_content" | sed 's/`\([^`]*\)`/<code>\1<\/code>/g')
    
    # 图片转换
    body_content=$(echo "$body_content" | sed 's/!\[\([^]]*\)\](\([^)]*\))/<img src="\2" alt="\1" \/>/g')
    
    # 链接转换
    body_content=$(echo "$body_content" | sed 's/\[\([^]]*\)\](\([^)]*\))/<a href="\2">\1<\/a>/g')
    
    # 粗体和斜体
    body_content=$(echo "$body_content" | sed 's/\*\*\([^*]*\)\*\*/<strong>\1<\/strong>/g')
    body_content=$(echo "$body_content" | sed 's/\*\([^*]*\)\*/<em>\1<\/em>/g')
    
    # 段落转换（简单处理）
    body_content=$(echo "$body_content" | sed 's/^[^-#<>].*$/<p>&<\/p>/g')
    
    echo "$body_content" >> "$output"
    
    # 关闭 HTML
    cat >> "$output" << 'HTML_FOOT'
</body>
</html>
HTML_FOOT

    echo -e "${GREEN}✓ 转换完成：$output${NC}"
}

# 转换为知乎格式
convert_to_zhihu() {
    local input="$1"
    local output="$2"
    
    echo -e "${BLUE}转换为知乎格式...${NC}"
    
    # 知乎支持 Markdown，直接复制并优化
    cp "$input" "$output"
    
    # 可以在这里添加知乎特定的优化
    # 比如添加知乎特定的标签、格式等
    
    echo -e "${GREEN}✓ 转换完成：$output${NC}"
}

# 转换为标准 HTML
convert_to_html() {
    local input="$1"
    local output="$2"
    
    echo -e "${BLUE}转换为 HTML...${NC}"
    convert_to_wechat "$input" "$output"
}

# 主函数
main() {
    parse_args "$@"
    
    # 确定输出文件
    if [ -z "$OUTPUT_FILE" ]; then
        local basename=$(basename "$INPUT_FILE" .md)
        case $TARGET in
            wechat)
                OUTPUT_FILE="${basename}-wechat.html"
                ;;
            zhihu)
                OUTPUT_FILE="${basename}-zhihu.md"
                ;;
            html)
                OUTPUT_FILE="${basename}.html"
                ;;
        esac
    fi
    
    # 提取封面图（如果请求）
    if [ "$EXTRACT_COVER" = true ]; then
        cover=$(extract_cover "$INPUT_FILE")
        if [ -n "$cover" ]; then
            echo -e "${BLUE}封面图：$cover${NC}"
        else
            echo -e "${YELLOW}未找到封面图${NC}"
        fi
    fi
    
    # 执行转换
    case $TARGET in
        wechat)
            convert_to_wechat "$INPUT_FILE" "$OUTPUT_FILE"
            ;;
        zhihu)
            convert_to_zhihu "$INPUT_FILE" "$OUTPUT_FILE"
            ;;
        html)
            convert_to_html "$INPUT_FILE" "$OUTPUT_FILE"
            ;;
        *)
            echo -e "${RED}错误：不支持的目标平台 $TARGET${NC}"
            echo "支持的平台：wechat, zhihu, html"
            exit 1
            ;;
    esac
    
    echo -e "${GREEN}🎉 转换成功！${NC}"
}

main "$@"
