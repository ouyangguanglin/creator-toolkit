# 🦞 虾王出品 · OpenClaw 入门教程 第 07 期

## Agent 的"双手"：文件操作

---

## 📌 核心概念

就像人类用手操作物理世界，Agent 通过文件操作来"触碰"数字世界。读写文件是 Agent 最基本也最重要的能力之一。

## 🎯 为什么文件操作如此重要？

**没有文件能力的 Agent：**
- 无法保存工作成果
- 无法读取配置和数据
- 每次对话都是"失忆"状态

**有文件能力的 Agent：**
- 可以持久化记忆
- 能够处理批量数据
- 自动维护配置和日志
- 与其他工具协作

## 🔧 OpenClaw 的文件操作工具

### 1. read - 读取文件

```bash
# 读取整个文件
openclaw read path/to/file.md

# 读取大文件的部分内容
openclaw read path/to/large.log --offset 1000 --limit 100
```

**适用场景：**
- 读取配置文件
- 查看日志内容
- 加载数据文件

### 2. write - 写入文件

```bash
# 创建或覆盖文件
openclaw write --path output.md --content "# Hello World"

# 注意：write 会覆盖原文件！
```

**适用场景：**
- 生成报告
- 保存配置
- 创建文档

### 3. edit - 精确编辑

```bash
# 替换文件中的特定文本
openclaw edit \
  --path config.json \
  --oldText '"debug": true' \
  --newText '"debug": false'
```

**适用场景：**
- 修改配置
- 更新文档
- 修复代码

**⚠️ 重要：** oldText 必须完全匹配（包括空格和缩进）

### 4. exec - 执行命令

```bash
# 执行 shell 命令
openclaw exec "ls -la"
openclaw exec "git status"

# 后台执行长任务
openclaw exec "npm run build" --background
```

**适用场景：**
- 版本控制
- 构建和部署
- 系统管理

## 💡 实战案例

### 案例 1：自动日志轮转

```bash
# 读取当前日志
openclaw read logs/app.log --limit 1000

# 归档旧日志
openclaw exec "mv logs/app.log logs/app.log.bak"

# 创建新日志文件
openclaw write --path logs/app.log --content "# New log started"
```

### 案例 2：批量配置文件更新

```bash
# 读取配置
openclaw read config/settings.json

# 修改特定值
openclaw edit \
  --path config/settings.json \
  --oldText '"apiVersion": "v1"' \
  --newText '"apiVersion": "v2"'

# 验证修改
openclaw exec "cat config/settings.json"
```

### 案例 3：自动化报告生成

```bash
# 收集数据
openclaw exec "git log --oneline -10"

# 生成报告
openclaw write --path reports/weekly.md --content "
# 周报

## 本周提交
$(git log --oneline -10)

## 待办事项
- [ ] 任务 1
- [ ] 任务 2
"
```

## ⚠️ 注意事项

1. **备份重要文件** - edit 和 write 可能意外覆盖
2. **权限检查** - 确保 Agent 有读写权限
3. **路径安全** - 避免路径遍历攻击
4. **敏感信息** - 不要将密钥写入日志
5. **并发冲突** - 多个 Agent 同时修改同一文件可能冲突

## 🚀 进阶技巧

### 组合使用模式

**读取 → 修改 → 验证 → 写入**

```bash
# 1. 读取
openclaw read config.json

# 2. 修改
openclaw edit --path config.json --oldText "..." --newText "..."

# 3. 验证
openclaw exec "jq . config.json"

# 4. 提交
openclaw exec "git add config.json && git commit -m 'Update config'"
```

### 安全编辑模式

对于重要配置，使用"先备份后修改"：

```bash
# 备份
openclaw exec "cp config.json config.json.bak"

# 修改
openclaw edit --path config.json ...

# 验证失败时恢复
openclaw exec "cp config.json.bak config.json"
```

---

## 🦞 互动话题

**你最常用文件操作做什么？**
- 配置文件管理？
- 日志分析？
- 批量数据处理？
- 自动化报告？

分享你的使用场景，我们一起讨论最佳实践！

---
#OpenClaw #文件操作 #自动化 #教程 #虾王出品
