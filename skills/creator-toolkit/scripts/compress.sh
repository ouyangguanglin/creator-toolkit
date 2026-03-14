#!/bin/bash
# Creator Toolkit - 批量图片压缩脚本
# 🖼️ 支持 JPG/PNG/WebP 格式，保持质量的同时减小文件大小

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 默认参数
QUALITY=85
FORMAT="webp"
OUTPUT_DIR=""
RESIZE=""

# 打印帮助信息
print_help() {
    cat << EOF
🖼️ 批量图片压缩

用法：$0 <输入目录> [选项]

选项:
  --quality <1-100>    图片质量（默认：85）
  --format <jpg|png|webp>  输出格式（默认：webp）
  --output <dir>       输出目录（默认：原目录下 created-compressed 文件夹）
  --resize <WxH>       调整尺寸，如 1920x1080
  --help               显示帮助信息

示例:
  $0 ./images --quality 80
  $0 ./photos --format jpg --quality 90
  $0 ./pics --resize 1920x1080 --output ./compressed

EOF
}

# 解析参数
parse_args() {
    INPUT_DIR=""
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --quality)
                QUALITY="$2"
                shift 2
                ;;
            --format)
                FORMAT="$2"
                shift 2
                ;;
            --output)
                OUTPUT_DIR="$2"
                shift 2
                ;;
            --resize)
                RESIZE="$2"
                shift 2
                ;;
            --help)
                print_help
                exit 0
                ;;
            *)
                if [ -z "$INPUT_DIR" ]; then
                    INPUT_DIR="$1"
                else
                    echo -e "${RED}错误：未知参数 $1${NC}"
                    print_help
                    exit 1
                fi
                shift
                ;;
        esac
    done
    
    if [ -z "$INPUT_DIR" ]; then
        echo -e "${RED}错误：请指定输入目录${NC}"
        print_help
        exit 1
    fi
    
    if [ ! -d "$INPUT_DIR" ]; then
        echo -e "${RED}错误：目录不存在 $INPUT_DIR${NC}"
        exit 1
    fi
}

# 检查依赖
check_dependencies() {
    # 检查 ffmpeg（用于图片处理）
    if ! command -v ffmpeg &> /dev/null; then
        echo -e "${YELLOW}警告：ffmpeg 未安装，尝试使用 ImageMagick...${NC}"
        if ! command -v convert &> /dev/null; then
            echo -e "${RED}错误：需要安装 ffmpeg 或 ImageMagick${NC}"
            echo "安装 ffmpeg: brew install ffmpeg (macOS) 或 apt install ffmpeg (Linux)"
            exit 1
        fi
        USE_IMAGEMAGICK=true
    else
        USE_IMAGEMAGICK=false
    fi
}

# 压缩单张图片（使用 ffmpeg）
compress_with_ffmpeg() {
    local input="$1"
    local output="$2"
    
    local resize_opt=""
    if [ -n "$RESIZE" ]; then
        resize_opt="-vf scale=$RESIZE"
    fi
    
    ffmpeg -i "$input" -q:v $QUALITY $resize_opt "$output" -y 2>/dev/null
}

# 压缩单张图片（使用 ImageMagick）
compress_with_imagemagick() {
    local input="$1"
    local output="$2"
    
    local resize_opt=""
    if [ -n "$RESIZE" ]; then
        resize_opt="-resize $RESIZE"
    fi
    
    convert "$input" -quality $QUALITY $resize_opt "$output" 2>/dev/null
}

# 主压缩函数
compress_images() {
    local count=0
    local success=0
    local failed=0
    local total_size_before=0
    local total_size_after=0
    
    echo -e "${BLUE}开始压缩图片...${NC}"
    echo -e "输入目录：$INPUT_DIR"
    echo -e "输出格式：$FORMAT"
    echo -e "质量：$QUALITY"
    if [ -n "$RESIZE" ]; then
        echo -e "调整尺寸：$RESIZE"
    fi
    echo ""
    
    # 创建输出目录
    if [ -z "$OUTPUT_DIR" ]; then
        OUTPUT_DIR="$INPUT_DIR/compressed"
    fi
    mkdir -p "$OUTPUT_DIR"
    
    # 查找并处理图片
    for img in "$INPUT_DIR"/*.{jpg,jpeg,png,gif,bmp,webp,JPG,JPEG,PNG,GIF,BMP,WEBP} 2>/dev/null; do
        if [ -f "$img" ]; then
            ((count++))
            
            # 检查免费版限制
            if [ $count -gt 10 ]; then
                echo -e "${YELLOW}⚠️  免费版限制：最多处理 10 张图片${NC}"
                echo -e "升级到 Pro 解锁无限批量处理：¥29/月"
                break
            fi
            
            filename=$(basename "$img")
            name="${filename%.*}"
            output_file="$OUTPUT_DIR/${name}.${FORMAT}"
            
            # 获取原始大小
            size_before=$(stat -f%z "$img" 2>/dev/null || stat -c%s "$img" 2>/dev/null || echo 0)
            total_size_before=$((total_size_before + size_before))
            
            echo -e "[$count] 处理：$filename"
            
            # 压缩
            if [ "$USE_IMAGEMAGICK" = true ]; then
                compress_with_imagemagick "$img" "$output_file"
            else
                compress_with_ffmpeg "$img" "$output_file"
            fi
            
            if [ -f "$output_file" ]; then
                ((success++))
                size_after=$(stat -f%z "$output_file" 2>/dev/null || stat -c%s "$output_file" 2>/dev/null || echo 0)
                total_size_after=$((total_size_after + size_after))
                
                # 计算压缩率
                if [ $size_before -gt 0 ]; then
                    ratio=$(echo "scale=2; (1 - $size_after / $size_before) * 100" | bc 2>/dev/null || echo "N/A")
                    echo -e "  ${GREEN}✓ 完成${NC} - 压缩率：${ratio}% (${size_before}B → ${size_after}B)"
                fi
            else
                ((failed++))
                echo -e "  ${RED}✗ 失败${NC}"
            fi
        fi
    done
    
    # 打印统计
    echo ""
    echo -e "${BLUE}=== 压缩完成 ===${NC}"
    echo -e "处理图片：$count 张"
    echo -e "成功：${GREEN}$success${NC}"
    echo -e "失败：${RED}$failed${NC}"
    
    if [ $total_size_before -gt 0 ]; then
        total_ratio=$(echo "scale=2; (1 - $total_size_after / $total_size_before) * 100" | bc 2>/dev/null || echo "N/A")
        echo -e "总大小：$(echo "scale=2; $total_size_before/1024" | bc)KB → $(echo "scale=2; $total_size_after/1024" | bc)KB"
        echo -e "总压缩率：${GREEN}${total_ratio}%${NC}"
    fi
    
    echo -e "输出目录：${BLUE}$OUTPUT_DIR${NC}"
}

# 主函数
main() {
    parse_args "$@"
    check_dependencies
    compress_images
}

main "$@"
