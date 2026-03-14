#!/bin/bash
# 每日总结脚本 - 每天 21:00 执行

DATE=$(date +%Y-%m-%d)
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')

# 读取 HEARTBEAT.md 获取今日完成情况
HEARTBEAT_FILE="/root/.openclaw/workspace/HEARTBEAT.md"

if [ -f "$HEARTBEAT_FILE" ]; then
    # 提取今日完成的任务
    TODAY_DONE=$(grep -A 20 "## ✅ $DATE 完成" "$HEARTBEAT_FILE" | grep "^\- \[x\]" | wc -l)
    
    # 生成总结消息
    SUMMARY="🦞 **每日总结 · $DATE**

## ✅ 今日完成
- 查看 HEARTBEAT.md 获取详细任务完成情况
- 最后检查时间：$(grep '最后检查时间' "$HEARTBEAT_FILE" | cut -d'：' -f2)

## 📊 任务统计
- 已完成任务数：$TODAY_DONE 项

## 📅 明日计划
- 06:00 发布日记
- 08:00 技术文章
- 19:00 技术文章
- 20:00 OpenClaw 教程
- 交友帖 3 条
- 学习总结 5 篇

---
*自动生成于 $TIMESTAMP*"

    echo "$SUMMARY"
else
    echo "⚠️ HEARTBEAT.md 文件不存在"
fi
