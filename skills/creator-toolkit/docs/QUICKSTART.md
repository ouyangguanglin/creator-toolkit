# 🚀 快速开始指南

欢迎使用 **Creator Toolkit**！5 分钟上手，开始提升你的创作效率。

---

## 📋 前置要求

### 必需
- **Java 17+** - [下载链接](https://adoptium.net/)
- **Bash** (Linux/macOS) 或 **Git Bash** (Windows)

### 可选（用于图片处理）
- **ffmpeg** - `brew install ffmpeg` (macOS) 或 `apt install ffmpeg` (Linux)
- **ImageMagick** - `brew install imagemagick` (macOS) 或 `apt install imagemagick` (Linux)

---

## ⚡ 安装（3 步）

### 方式 1：使用 skillhub（推荐）

```bash
# 安装技能
skillhub install creator-toolkit

# 验证安装
creator-toolkit version
```

### 方式 2：手动安装

```bash
# 1. 克隆仓库
git clone https://github.com/yourusername/creator-toolkit.git
cd creator-toolkit

# 2. 运行安装脚本
./scripts/install.sh

# 3. 验证安装
./scripts/main.sh version
```

### 方式 3：使用 Java JAR

```bash
# 1. 编译 Java 代码
cd java
mvn clean package

# 2. 运行
java -jar target/creator-toolkit-0.1.0-jar-with-dependencies.jar version
```

---

## 🔧 配置

### 1. 复制配置模板

```bash
cp config.example.json config.json
```

### 2. 编辑配置

```bash
vim config.json
```

### 3. 填写必要信息

```json
{
  "wechat": {
    "appId": "你的公众号 AppID",
    "appSecret": "你的公众号 AppSecret"
  },
  "image": {
    "defaultQuality": 85,
    "defaultFormat": "webp"
  }
}
```

**注意：** 如果暂时不发布到平台，可以留空，不影响图片处理功能。

---

## 📖 使用示例

### 🖼️ 批量压缩图片

```bash
# 压缩当前目录下所有图片
./scripts/main.sh compress ./images

# 指定质量（1-100）
./scripts/main.sh compress ./images --quality 80

# 转换为 JPG 格式
./scripts/main.sh compress ./images --format jpg

# 调整尺寸
./scripts/main.sh compress ./images --resize 1920x1080

# 指定输出目录
./scripts/main.sh compress ./images --output ./compressed
```

### 📝 文章格式转换

```bash
# 转换为微信公众号格式
./scripts/main.sh convert ./article.md --target wechat

# 转换为知乎格式
./scripts/main.sh convert ./article.md --target zhihu

# 转换为标准 HTML
./scripts/main.sh convert ./article.md --target html

# 自动提取封面图
./scripts/main.sh convert ./article.md --target wechat --extract-cover
```

### 📤 发布文章

```bash
# 发布到微信公众号
./scripts/main.sh publish ./article.md --platforms wechat

# 发布到多个平台
./scripts/main.sh publish ./article.md --platforms wechat,zhihu,juejin

# 添加标签
./scripts/main.sh publish ./article.md --platforms wechat --tags "Java,编程"

# 指定标题
./scripts/main.sh publish ./article.md --platforms wechat --title "我的文章"

# 定时发布（Pro 功能）
./scripts/main.sh publish ./article.md --platforms wechat --schedule "2026-03-14 09:00"
```

---

## 💰 升级 Pro

### 免费版限制
- 批量处理：最多 10 张/次
- 发布平台：最多 2 个
- 定时发布：❌
- 数据看板：❌

### Pro 版价格
- **月付**：¥29/月
- **年付**：¥299/年（省¥49）
- **终身**：¥599

### 购买方式

1. 联系邮箱：your-email@example.com
2. 获取授权码
3. 激活授权：

```bash
./scripts/main.sh license activate PRO-XXXX-XXXX-XXXX
```

---

## ❓ 常见问题

### Q: 安装后提示找不到命令？

**A:** 确保脚本有执行权限：

```bash
chmod +x scripts/*.sh
```

### Q: 图片压缩失败？

**A:** 检查是否安装了 ffmpeg 或 ImageMagick：

```bash
# 检查 ffmpeg
ffmpeg -version

# 检查 ImageMagick
convert -version
```

### Q: 发布功能无法使用？

**A:** 发布功能需要配置各平台的 API 密钥。如果暂时不需要，可以跳过，不影响其他功能。

### Q: 如何卸载？

```bash
# skillhub 安装
skillhub uninstall creator-toolkit

# 手动安装
rm -rf creator-toolkit
```

---

## 📚 下一步

- 📖 [详细文档](./docs/tutorial.md)
- 🔧 [配置说明](./docs/config.md)
- 💻 [API 参考](./docs/api.md)
- 🤝 [贡献指南](./CONTRIBUTING.md)

---

## 🆘 获取帮助

- 📧 邮箱：your-email@example.com
- 🐛 Issues: [GitHub Issues](https://github.com/yourusername/creator-toolkit/issues)
- 💬 讨论区：[GitHub Discussions](https://github.com/yourusername/creator-toolkit/discussions)

---

**祝你创作愉快！** 🦞
