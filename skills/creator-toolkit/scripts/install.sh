#!/bin/bash
# Creator Toolkit - 安装脚本

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"

echo "🧰 安装 Creator Toolkit..."

# 检查 Java
if ! command -v java &> /dev/null; then
    echo "❌ 需要 Java 环境"
    echo "请安装 Java 17+: https://adoptium.net/"
    exit 1
fi

echo "✓ Java 已安装：$(java -version 2>&1 | head -1)"

# 检查 ffmpeg（可选）
if command -v ffmpeg &> /dev/null; then
    echo "✓ ffmpeg 已安装（图片处理）"
elif command -v convert &> /dev/null; then
    echo "✓ ImageMagick 已安装（图片处理）"
else
    echo "⚠️  未找到 ffmpeg 或 ImageMagick"
    echo "图片压缩功能将不可用"
    echo "安装 ffmpeg: brew install ffmpeg (macOS) 或 apt install ffmpeg (Linux)"
fi

# 设置执行权限
chmod +x "$SCRIPT_DIR"/*.sh
echo "✓ 设置脚本执行权限"

# 创建配置
if [ ! -f "$ROOT_DIR/config.json" ]; then
    cp "$ROOT_DIR/config.example.json" "$ROOT_DIR/config.json"
    echo "✓ 创建配置文件"
fi

# 创建符号链接（可选）
if [ -w "/usr/local/bin" ] || [ -w "$HOME/.local/bin" ]; then
    LINK_DIR="/usr/local/bin"
    [ ! -w "/usr/local/bin" ] && LINK_DIR="$HOME/.local/bin"
    
    ln -sf "$SCRIPT_DIR/main.sh" "$LINK_DIR/creator-toolkit" 2>/dev/null || true
    echo "✓ 创建命令行快捷方式：creator-toolkit"
fi

echo ""
echo "🎉 安装完成！"
echo ""
echo "使用方法："
echo "  creator-toolkit help          # 查看帮助"
echo "  creator-toolkit compress      # 压缩图片"
echo "  creator-toolkit convert       # 格式转换"
echo "  creator-toolkit publish       # 发布文章"
echo ""
echo "配置："
echo "  编辑 $ROOT_DIR/config.json 填写你的 API 密钥"
echo ""
