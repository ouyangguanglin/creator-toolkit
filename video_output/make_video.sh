#!/bin/bash
# 牧岚视频号 - 降压指南 视频合成脚本

FRAMES_DIR="/root/.openclaw/workspace/video_output/frames"
OUTPUT_DIR="/root/.openclaw/workspace/video_output"
VIDEO_OUTPUT="$OUTPUT_DIR/mulan_bp_video.mp4"

# 创建帧列表文件（每个帧的持续时间）
# 格式：file 'filename' + duration in seconds
cat > "$OUTPUT_DIR/frame_list.txt" << EOF
file 'frames/frame_001.png'
duration 15
file 'frames/frame_002.png'
duration 15
file 'frames/frame_003.png'
duration 30
file 'frames/frame_004.png'
duration 20
file 'frames/frame_005.png'
duration 10
file 'frames/frame_006.png'
duration 10
file 'frames/frame_007.png'
duration 15
file 'frames/frame_008.png'
duration 10
file 'frames/frame_009.png'
duration 5
EOF

echo "开始合成视频..."

# 使用 FFmpeg 合成视频
ffmpeg -y -f concat -safe 0 -i "$OUTPUT_DIR/frame_list.txt" \
    -vf "scale=1080:1920:force_original_aspect_ratio=decrease,pad=1080:1920:(ow-iw)/2:(oh-ih)/2" \
    -c:v libvpx-vp9 -pix_fmt yuv420p -r 30 \
    -b:v 2M \
    "$OUTPUT_DIR/video_no_audio.webm"

echo "✓ 视频合成完成（无音频）"
echo "视频文件：$OUTPUT_DIR/video_no_audio.mp4"

# 显示视频信息
ffprobe -v quiet -show_format -show_streams "$OUTPUT_DIR/video_no_audio.mp4" | grep -E "(duration|width|height|codec_name)"
