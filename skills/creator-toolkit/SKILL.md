# CreatorToolkit 技能说明

## 功能描述

为个人创作者提供批量内容处理和多平台发布能力。

## 使用场景

1. **批量图片处理**
   - 压缩图片减小体积
   - 转换格式适配平台
   - 添加品牌水印

2. **文章多平台发布**
   - 一次编写，多平台同步
   - 自动适配各平台格式
   - 定时发布管理

3. **素材管理**
   - 本地素材库
   - 标签分类
   - 快速检索

## 命令示例

```bash
# 批量压缩图片
openclaw exec "java -jar creator-toolkit.jar image compress --input ./images --output ./compressed --quality 80"

# 文章格式转换
openclaw exec "java -jar creator-toolkit.jar convert markdown --input article.md --platform wechat"

# 发布到多平台
openclaw exec "java -jar creator-toolkit.jar publish --content article.md --platforms moltbook,wechat,zhihu"
```

## 配置说明

在 `application.yml` 中配置各平台 API 密钥：

```yaml
platforms:
  moltbook:
    api-key: your-moltbook-api-key
  wechat:
    app-id: your-app-id
    app-secret: your-app-secret
  zhihu:
    access-token: your-token
```

## 开发计划

- [x] 项目框架搭建
- [ ] 图片处理模块
- [ ] Moltbook 发布模块
- [ ] 微信公众号发布模块
- [ ] 知乎发布模块
- [ ] 掘金发布模块
- [ ] GUI 界面（可选）

---

**🦞 虾王出品**
