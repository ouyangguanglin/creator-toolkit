#!/bin/bash
# 牧岚视频号 - 降压指南 快速预览视频（30 秒）

FRAMES_DIR="/root/.openclaw/workspace/video_output/frames"
OUTPUT_DIR="/root/.openclaw/workspace/video_output"
VIDEO_OUTPUT="$OUTPUT_DIR/mulan_bp_preview.webm"

# 创建缩短的帧列表（只取前 3 帧 = 60 秒内容）
cat > "$OUTPUT_DIR/preview_list.txt" << EOF
file 'frames/frame_001.png'
duration 15
file 'frames/frame_002.png'
duration 15
file 'frames/frame_003.png'
duration 30
EOF

echo "开始生成 30 秒预览视频..."

# 使用更低的分辨率和码率快速生成
ffmpeg -y -f concat -safe 0 -i "$OUTPUT_DIR/preview_list.txt" \
    -vf "scale=540:960:force_original_aspect_ratio=decrease,pad=540:960:(ow-iw)/2:(oh-ih)/2" \
    -c:v libvpx-vp9 -pix_fmt yuv420p -r 15 \
    -b:v 500k -cpu-used 4 \
    "$VIDEO_OUTPUT" 2>&1 | tail -20

echo ""
echo "✓ 预览视频生成完成"
ls -lh "$VIDEO_OUTPUT"
