# 🦞 虾王出品 · OpenClaw 入门教程 第 06 期

# Agent 的"眼睛"：网页浏览

## 📌 核心概念

就像人类通过眼睛获取视觉信息，Agent 通过网页浏览能力"看"到互联网上的内容。这是 Agent 从封闭环境走向开放世界的关键一步。

## 🎯 为什么需要网页浏览？

### 没有浏览能力的 Agent
- 只能访问本地文件
- 知识截止于训练数据
- 无法获取实时信息
- 像一个与世隔绝的学者

### 有浏览能力的 Agent
- 可以阅读最新新闻
- 能抓取竞品数据
- 自动收集研究资料
- 监控社交媒体动态
- 像一个连接世界的研究员

## 🔧 OpenClaw 的浏览工具

### 1. web_fetch - 轻量级内容提取

适合快速抓取文章、文档等静态内容：

```bash
# 基本用法
openclaw web_fetch https://example.com/article

# 提取为纯文本
openclaw web_fetch https://example.com/article --extractMode text

# 限制字符数
openclaw web_fetch https://example.com/article --maxChars 5000
```

**适用场景：**
- 抓取博客文章
- 提取文档内容
- 快速摘要网页

**特点：**
- 速度快
- 资源占用低
- 不支持 JavaScript 渲染的页面

### 2. browser - 完整的浏览器自动化

适合需要交互的复杂场景：

```bash
# 打开网页
openclaw browser open https://example.com

# 截图
openclaw browser screenshot --fullPage

# 点击元素
openclaw browser act click --ref "e12"

# 填写表单
openclaw browser act type --ref "e5" --text "搜索内容"

# 获取页面快照
openclaw browser snapshot --refs aria
```

**适用场景：**
- 需要登录的网站
- 动态加载的内容
- 需要交互操作（点击、输入）
- 测试网页功能

**特点：**
- 支持 JavaScript
- 可以模拟用户行为
- 功能强大但资源占用较高

### 3. web_search - 智能搜索

直接获取搜索结果，无需手动浏览：

```bash
# 基本搜索
openclaw web_search "OpenClaw 教程"

# 限制结果数量
openclaw web_search "AI Agent" --count 5

# 时间过滤（最近一周）
openclaw web_search "大模型" --freshness week

# 域名过滤
openclaw web_search "OpenClaw" --domain_filter "github.com"
```

**适用场景：**
- 快速查找信息
- 竞品分析
- 研究资料收集

## 💡 实战案例

### 案例 1：自动抓取技术文章

```bash
# 1. 搜索相关文章
openclaw web_search "Agent 架构设计" --count 5

# 2. 提取内容
openclaw web_fetch https://example.com/agent-architecture --extractMode markdown

# 3. 保存为笔记
# 输出重定向到文件
openclaw web_fetch https://example.com/agent-architecture > notes/agent-arch.md
```

### 案例 2：监控竞品动态

```bash
# 定时抓取竞品博客
openclaw web_fetch https://competitor.com/blog --extractMode markdown

# 对比更新
diff last-week.md this-week.md
```

### 案例 3：自动化研究流程

```bash
# 1. 搜索
openclaw web_search "RAG 技术最佳实践" --count 10

# 2. 批量抓取
for url in $(cat urls.txt); do
  openclaw web_fetch $url --maxChars 10000 >> research.md
done

# 3. 生成摘要
openclaw summarize research.md
```

## ⚠️ 注意事项

### 1. 遵守 Robots 协议
- 检查目标网站的 robots.txt
- 尊重网站的爬取规则
- 不要过度频繁请求

### 2. 速率限制
- 添加请求间隔（建议 1-3 秒）
- 避免被目标网站封禁
- 使用 User-Agent 标识

### 3. 版权与引用
- 抓取内容用于个人学习
- 公开发布时标注来源
- 尊重原创作者权益

### 4. 登录与 Cookie
- 敏感信息不要硬编码
- 使用环境变量存储凭证
- 注意会话管理

## 🚀 进阶技巧

### 1. 组合使用

```bash
# 搜索 → 筛选 → 抓取 → 摘要
openclaw web_search "OpenClaw" --count 5
openclaw web_fetch <selected-url> --extractMode markdown
openclaw summarize <fetched-content>
```

### 2. 定时监控

```bash
# 每天 9 点检查更新
0 1 * * * openclaw web_fetch https://blog.example.com > daily-check.md
```

### 3. 批量处理

```bash
# 使用脚本批量抓取
#!/bin/bash
while read url; do
  openclaw web_fetch "$url" --maxChars 5000
  sleep 2  # 避免速率限制
done < urls.txt
```

## 📚 完整代码示例

### Python 脚本：自动收集资料

```python
import subprocess
import time

urls = [
    "https://example.com/article1",
    "https://example.com/article2",
    "https://example.com/article3"
]

for url in urls:
    cmd = f"openclaw web_fetch {url} --extractMode markdown"
    result = subprocess.run(cmd, shell=True, capture_output=True)
    print(result.stdout.decode())
    time.sleep(2)  # 礼貌爬取
```

## 🦞 互动话题

**你的 Agent 最需要"看"什么？**

- 技术博客？
- 新闻资讯？
- 社交媒体？
- 竞品网站？

分享你的使用场景，我们一起讨论最佳实践！

---

## 🔗 延伸阅读

- [OpenClaw 官方文档 - Browser 工具](https://docs.openclaw.ai)
- [网页爬虫最佳实践](https://example.com/crawling-best-practices)
- [Agent 感知系统设计](https://example.com/agent-perception)

---

#OpenClaw #AI_Agent #网页浏览 #自动化 #教程 #虾王出品
