#!/bin/bash
# 牧岚视频号 - 降压指南 完整视频合成（带配音）

OUTPUT_DIR="/root/.openclaw/workspace/video_output"
AUDIO_FILE="$OUTPUT_DIR/voiceover.opus"
VIDEO_OUTPUT="$OUTPUT_DIR/mulan_bp_full.webm"

# 获取音频时长
AUDIO_DURATION=$(ffprobe -v quiet -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "$AUDIO_FILE")
echo "音频时长：${AUDIO_DURATION}秒"

# 创建与音频时长匹配的帧列表
# 总共 9 帧，按比例分配时间
cat > "$OUTPUT_DIR/full_list.txt" << EOF
file 'frames/frame_001.png'
duration 3
file 'frames/frame_002.png'
duration 3
file 'frames/frame_003.png'
duration 4
file 'frames/frame_004.png'
duration 2
file 'frames/frame_005.png'
duration 1.5
file 'frames/frame_006.png'
duration 1.5
file 'frames/frame_007.png'
duration 2
file 'frames/frame_008.png'
duration 1.5
file 'frames/frame_009.png'
duration 1.5
EOF

echo "开始合成完整视频（带配音）..."

# 合成视频 + 音频
ffmpeg -y \
    -f concat -safe 0 -i "$OUTPUT_DIR/full_list.txt" \
    -i "$AUDIO_FILE" \
    -vf "scale=1080:1920:force_original_aspect_ratio=decrease,pad=1080:1920:(ow-iw)/2:(oh-ih)/2" \
    -c:v libvpx-vp9 -pix_fmt yuv420p -r 30 -b:v 2M \
    -c:a copy \
    -shortest \
    "$VIDEO_OUTPUT" 2>&1 | tail -15

echo ""
echo "✓ 完整视频生成完成"
ls -lh "$VIDEO_OUTPUT"

# 显示视频信息
echo ""
echo "视频信息："
ffprobe -v quiet -show_format -show_streams "$VIDEO_OUTPUT" 2>&1 | grep -E "(duration|width|height|codec_name|bit_rate)" | head -10
