# 更新日志

所有重要变更将记录在此文件中。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

---

## [未发布]

### 计划中
- 🖼️ WebP 格式完整支持
- 📤 小红书发布支持
- 📊 数据看板功能
- 🔐 用户认证系统
- 💰 支付集成

---

## [0.1.0] - 2026-03-13

### ✨ 新增
- 🎉 项目初始发布
- 🖼️ 批量图片压缩功能
  - 支持 JPG/PNG/WebP 格式
  - 可调节质量（1-100）
  - 支持调整尺寸
  - 免费版限 10 张/次
- 📝 文章格式转换
  - Markdown → 微信公众号格式
  - Markdown → 知乎格式
  - Markdown → HTML
  - 自动提取封面图
- 📤 多平台发布框架
  - 微信公众号（演示）
  - 知乎（演示）
  - 掘金（演示）
- 💰 授权管理系统
  - 免费版/Pro 版区分
  - 授权码验证
  - 功能限制控制
- 🔐 用户认证系统
  - 用户注册/登录
  - 会话管理
  - 密码加密存储

### 🔧 技术
- Java 17 核心实现
- Bash 脚本封装
- Maven 构建系统
- OkHttp HTTP 客户端
- Flexmark Markdown 解析
- Imgscalr 图片处理

### 📚 文档
- README.md 项目说明
- QUICKSTART.md 快速开始
- SKILL.md 技能文档
- 示例文章

### 🐛 已知问题
- 微信公众号发布需要手动配置 API
- 知乎发布需要手动配置 API
- 掘金发布需要手动配置 API
- WebP 格式需要额外依赖

---

## 版本说明

### 免费版
- ✅ 基础功能可用
- ⚠️ 批量处理限 10 张/次
- ⚠️ 发布限 2 个平台
- ❌ 无定时发布
- ❌ 无数据看板

### Pro 版 (¥29/月)
- ✅ 无限批量处理
- ✅ 无限平台发布
- ✅ 定时发布
- ✅ 数据看板
- ✅ 优先技术支持

---

## 升级指南

### 0.0.x → 0.1.0

首次正式发布，无需升级步骤。

```bash
# 新用户安装
skillhub install creator-toolkit

# 或手动安装
git clone https://github.com/yourusername/creator-toolkit.git
cd creator-toolkit
./scripts/install.sh
```

---

## 贡献

欢迎提交 Issue 和 Pull Request！

---

*最后更新：2026-03-13*
