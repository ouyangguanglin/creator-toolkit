# 🦞 CreatorToolkit - 创作者工具箱

个人创作者的一站式内容处理与发布工具。

## 🎯 核心功能

### 1. 批量图片处理
- 批量压缩（WebP/PNG/JPG）
- 格式转换
- 自动水印

### 2. 文章格式转换
- Markdown → 微信公众号格式
- Markdown → 知乎格式
- Markdown → 掘金格式

### 3. 多平台发布
- Moltbook
- 微信公众号
- 知乎
- 掘金

## 💰 商业模式

| 版本 | 价格 | 功能 |
|------|------|------|
| 基础版 | 免费 | 基础图片处理 + Moltbook 发布 |
| Pro 版 | ¥29/月 | 全部平台发布 + 批量处理 |
| 终身版 | ¥299 | Pro 版 + 优先支持 + 定制功能 |

## 🛠️ 技术栈

- Java 17+
- Spring Boot 3.x
- OkHttp3（HTTP 客户端）
- Jackson（JSON 处理）
- Thumbnailator（图片处理）

## 📁 项目结构

```
creator-toolkit/
├── src/                    # Java 源代码
│   ├── main/
│   │   ├── java/com/creator/toolkit/
│   │   │   ├── image/     # 图片处理模块
│   │   │   ├── platform/  # 平台发布模块
│   │   │   ├── convert/   # 格式转换模块
│   │   │   └── CreatorToolkitApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── scripts/                # 脚本工具
├── docs/                   # 文档
└── README.md
```

## 🚀 快速开始

### 环境要求
- JDK 17+
- Maven 3.8+

### 构建
```bash
cd creator-toolkit
mvn clean package
```

### 运行
```bash
java -jar target/creator-toolkit-1.0.0.jar
```

## 📄 License

MIT License

---

**🦞 虾王出品 · 为创作者赋能**
