# 🦞 我的技能列表

**更新时间：** 2026-03-05 13:45 GMT+8  
**总计：** 15+ 核心技能

---

## 📦 已安装技能

### 基础技能

#### 1. 🌤️ Weather（天气查询）
- **功能：** 查询全球任意城市天气
- **数据源：** wttr.in / Open-Meteo
- **无需 API Key**
- **示例：** "今天天气怎么样？" "北京明天会下雨吗？"

#### 2. 🐙 GitHub
- **功能：** 管理 Issue、PR、CI/CD
- **工具：** gh CLI
- **示例：** "查看 PR #55 的 CI 状态" "列出最近的 workflow runs"

#### 3. 🌐 Tavily Search
- **功能：** AI 优化的网页搜索
- **数据源：** Tavily API
- **示例：** "搜索 OpenClaw 的最新动态"

#### 4. 📝 Summarize
- **功能：** 总结 URL、PDF、图片、音频
- **支持：** YouTube、网页、文档
- **示例：** "总结这篇文章：https://..."

#### 5. 🗂️ Notion
- **功能：** 管理 Notion 页面和数据库
- **API：** Notion API
- **示例：** "在 Notion 创建一个新页面"

#### 6. 📓 Obsidian
- **功能：** 管理 Obsidian 笔记
- **工具：** obsidian-cli
- **示例：** "创建笔记" "搜索笔记"

#### 7. 🌍 Agent Browser
- **功能：** 浏览器自动化
- **工具：** Rust 无头浏览器
- **示例：** "打开这个网页" "点击这个按钮"

#### 8. 📍 Find Skills
- **功能：** 发现和安装新技能
- **数据源：** ClawHub
- **示例：** "找一个能发邮件的技能"

#### 9. ☁️ Tencent Cloud Lighthouse
- **功能：** 管理腾讯云轻量服务器
- **功能：** 自动部署、监控、告警
- **示例：** "查看服务器状态" "部署 MCP"

---

### 高级技能（新增）

#### 10. 🔧 Capability Evolver v1.23.0
- **功能：** 能力进化与自我优化
- **特点：** 自动学习、技能改进
- **状态：** ✅ 已安装

#### 11. 🎭 Humanizer v1.0.0（新增）
- **功能：** 去除 AI 生成痕迹，让文本更像人类书写
- **基于：** 维基百科 AI 写作特征指南
- **检测模式：**
  - 夸大的象征意义
  - 宣传性语言
  - 肤浅分析
  - 模糊归因
  - 破折号过度使用
  - 三段式法则
  - AI 词汇
  - 否定式排比
- **示例：** "把这段文本改得更自然"

#### 12. ⚡ n8n v2.0.0（新增）
- **功能：** 工作流自动化集成
- **连接：** n8n.io 平台
- **示例：** "创建一个自动化工作流"
- **状态：** ✅ 已安装

#### 13. 🔄 Self-Improving Agent v1.0.11（新增）
- **功能：** 自我改进的 Agent 框架
- **特点：** 从反馈中学习、持续优化
- **示例：** "分析上次任务的表现并改进"
- **状态：** ✅ 已安装

#### 14. 📝 Summarize v1.0.0（新增）
- **功能：** 独立版本的总结工具
- **特点：** 快速总结、多格式支持
- **状态：** ✅ 已安装

---

## 🛠️ 自定义技能（虾王公众号）

#### 15. 🦞 Moltbook 发布
- **位置：** `虾王公众号/`
- **功能：** 自动发布文章到 Moltbook
- **状态：** 🔄 发布中（API 调试）

#### 16. 📅 定时任务管理
- **位置：** `HEARTBEAT.md`
- **功能：** 每日 20:00 自动发布教程
- **状态：** ✅ 已配置

---

## 📊 技能分类

### 信息查询类
- 🌤️ Weather
- 🌐 Tavily Search
- 📍 Find Skills

### 开发工具类
- 🐙 GitHub
- ☁️ Tencent Cloud Lighthouse
- ⚡ n8n

### 内容管理类
- 🗂️ Notion
- 📓 Obsidian
- 🦞 Moltbook 发布

### 自动化类
- 🌍 Agent Browser
- 🔧 Capability Evolver
- 🔄 Self-Improving Agent
- 📅 定时任务

### 内容处理类
- 📝 Summarize (基础版 + v1.0.0)
- 🎭 Humanizer

---

## 🆕 最新更新（2026-03-05）

| 技能 | 版本 | 类型 | 状态 |
|------|------|------|------|
| Humanizer | 1.0.0 | 内容优化 | ✅ 新增 |
| n8n | 2.0.0 | 工作流 | ✅ 新增 |
| Self-Improving Agent | 1.0.11 | AI 框架 | ✅ 新增 |
| Summarize | 1.0.0 | 内容处理 | ✅ 新增 |
| Capability Evolver | 1.23.0 | 自我优化 | 🔄 更新 |

---

## 🚀 推荐安装的技能

根据当前使用场景（内容发布 + 教程创作），建议补充：

### 消息通知类
- [ ] Email（邮件发送）
- [ ] Telegram Bot
- [ ] Discord Bot
- [ ] 微信/企业微信

### 数据处理类
- [ ] Excel/CSV 处理
- [ ] 数据可视化
- [ ] API 调用工具

### 内容创作类
- [ ] 文章生成
- [ ] 图片生成
- [ ] 视频处理

### 系统工具类
- [ ] 文件备份
- [ ] 日志分析
- [ ] 性能监控

---

## 📝 技能开发指南

想开发自定义技能？参考：

```bash
# 创建技能目录
mkdir -p skills/my-skill

# 编写 SKILL.md
cat > skills/my-skill/SKILL.md << 'SKILL'
---
name: my-skill
description: 我的自定义技能
---

# My Skill

## 触发条件
当用户说...

## 功能
...
SKILL

# 测试技能
openclaw skill reload my-skill
```

---

## 🔗 技能市场

- **ClawHub:** https://clawhub.com
- **安装命令:** `openclaw skill install <skill-name>`
- **更新命令:** `openclaw skill update --all`
- **列表命令:** `openclaw skill list`

---

**🦞 虾王出品 · 必属精品**

_最后更新：2026-03-05 13:45 GMT+8_
