#!/bin/bash
# 牧岚视频号 - 降压指南 卡通可爱版 完整视频合成

OUTPUT_DIR="/root/.openclaw/workspace/video_output"
AUDIO_FILE="$OUTPUT_DIR/voiceover.opus"
VIDEO_OUTPUT="$OUTPUT_DIR/mulan_bp_cute.webm"

# 创建帧列表
cat > "$OUTPUT_DIR/cute_list.txt" << EOF
file 'frames_v2/frame_001.png'
duration 3
file 'frames_v2/frame_002.png'
duration 3
file 'frames_v2/frame_003.png'
duration 4
file 'frames_v2/frame_004.png'
duration 2
file 'frames_v2/frame_005.png'
duration 2
file 'frames_v2/frame_006.png'
duration 2
file 'frames_v2/frame_007.png'
duration 2
file 'frames_v2/frame_008.png'
duration 1.5
file 'frames_v2/frame_009.png'
duration 1.5
EOF

echo "开始合成卡通可爱版视频..."

# 合成视频 + 音频
ffmpeg -y \
    -f concat -safe 0 -i "$OUTPUT_DIR/cute_list.txt" \
    -i "$AUDIO_FILE" \
    -vf "scale=1080:1920:force_original_aspect_ratio=decrease,pad=1080:1920:(ow-iw)/2:(oh-ih)/2" \
    -c:v libvpx-vp9 -pix_fmt yuv420p -r 30 -b:v 2M \
    -c:a copy \
    -shortest \
    "$VIDEO_OUTPUT" 2>&1 | tail -10

echo ""
echo "✓ 卡通可爱版视频生成完成"
ls -lh "$VIDEO_OUTPUT"
