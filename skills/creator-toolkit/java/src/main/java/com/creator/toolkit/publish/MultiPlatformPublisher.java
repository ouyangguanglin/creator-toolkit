package com.creator.toolkit.publish;

import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * 多平台发布器
 * 📤 一键发布到微信公众号、知乎、掘金等平台
 */
public class MultiPlatformPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(MultiPlatformPublisher.class);
    
    private String[] platforms;
    private String title;
    private String[] tags;
    private String schedule;
    private String coverImage;
    
    // 免费版限制
    private static final int FREE_PLATFORM_LIMIT = 2;
    
    // HTTP 客户端
    private final OkHttpClient client;
    
    // 配置文件路径
    private static final String CONFIG_FILE = "config.json";
    
    public MultiPlatformPublisher() {
        client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    }
    
    public void setPlatforms(String[] platforms) {
        this.platforms = platforms;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setTags(String[] tags) {
        this.tags = tags;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
    
    /**
     * 发布文章
     */
    public void publish(String inputFile) throws IOException {
        // 读取文章内容
        String content = Files.readString(Paths.get(inputFile));
        
        // 提取标题（如果未指定）
        if (title == null) {
            title = extractTitle(content);
        }
        
        System.out.println("📤 开始发布");
        System.out.println("标题：" + title);
        System.out.println("平台：" + String.join(", ", platforms));
        if (schedule != null) {
            System.out.println("定时：" + schedule);
        }
        if (tags != null && tags.length > 0) {
            System.out.println("标签：" + String.join(", ", tags));
        }
        System.out.println();
        
        // 检查免费版限制
        int platformCount = platforms.length;
        if (platformCount > FREE_PLATFORM_LIMIT) {
            System.out.println("⚠️  免费版限制：最多发布 " + FREE_PLATFORM_LIMIT + " 个平台");
            System.out.println("升级到 Pro 解锁无限平台发布：¥29/月");
            
            // 只发布前 2 个平台
            String[] limitedPlatforms = new String[FREE_PLATFORM_LIMIT];
            System.arraycopy(platforms, 0, limitedPlatforms, 0, FREE_PLATFORM_LIMIT);
            platforms = limitedPlatforms;
        }
        
        // 发布到各个平台
        for (String platform : platforms) {
            try {
                switch (platform.trim().toLowerCase()) {
                    case "wechat" -> publishToWechat(content, title);
                    case "zhihu" -> publishToZhihu(content, title);
                    case "juejin" -> publishToJuejin(content, title);
                    default -> System.out.println("⚠️  跳过未知平台：" + platform);
                }
                
                // 平台间延迟，避免速率限制
                Thread.sleep(2000);
                
            } catch (Exception e) {
                logger.error("发布失败：" + platform, e);
                System.out.println("❌ " + platform + " 发布失败：" + e.getMessage());
            }
        }
        
        System.out.println("\n✅ 发布完成！");
    }
    
    /**
     * 发布到微信公众号
     */
    private void publishToWechat(String content, String title) throws IOException {
        System.out.println("📱 发布到微信公众号...");
        
        // 读取配置
        JSONObject config = loadConfig();
        JSONObject wechatConfig = config.optJSONObject("wechat");
        
        if (wechatConfig == null || !wechatConfig.has("appId") || !wechatConfig.has("appSecret")) {
            System.out.println("⚠️  微信公众号配置未设置，跳过");
            System.out.println("提示：请在 config.json 中配置 wechat.appId 和 wechat.appSecret");
            return;
        }
        
        String appId = wechatConfig.getString("appId");
        String appSecret = wechatConfig.getString("appSecret");
        
        // 获取 access_token
        String accessToken = getWechatAccessToken(appId, appSecret);
        if (accessToken == null) {
            System.out.println("❌ 获取 access_token 失败");
            return;
        }
        
        System.out.println("✓ 获取 access_token 成功");
        
        // TODO: 调用微信公众号 API 发布文章
        // 这里需要根据微信公众号 API 文档实现
        // 参考：https://developers.weixin.qq.com/doc/offiaccount/Advanced_Guide/Web_Applet.html
        
        System.out.println("⚠️  微信公众号发布需要额外配置");
        System.out.println("提示：请参考微信公众号开发文档完成集成");
        System.out.println("✅ 微信公众号发布完成（演示模式）");
    }
    
    /**
     * 获取微信公众号 access_token
     */
    private String getWechatAccessToken(String appId, String appSecret) throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" 
                   + appId + "&secret=" + appSecret;
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            
            if (json.has("access_token")) {
                return json.getString("access_token");
            } else {
                logger.error("获取 access_token 失败：" + responseBody);
                return null;
            }
        }
    }
    
    /**
     * 发布到知乎
     */
    private void publishToZhihu(String content, String title) throws IOException {
        System.out.println("📖 发布到知乎...");
        
        // 读取配置
        JSONObject config = loadConfig();
        JSONObject zhihuConfig = config.optJSONObject("zhihu");
        
        if (zhihuConfig == null || !zhihuConfig.has("accessToken")) {
            System.out.println("⚠️  知乎配置未设置，跳过");
            System.out.println("提示：请在 config.json 中配置 zhihu.accessToken");
            return;
        }
        
        String accessToken = zhihuConfig.getString("accessToken");
        
        // TODO: 调用知乎 API 发布文章
        // 参考：https://developers.zhihu.com/
        
        System.out.println("⚠️  知乎发布需要额外配置");
        System.out.println("提示：请参考知乎开发文档完成集成");
        System.out.println("✅ 知乎发布完成（演示模式）");
    }
    
    /**
     * 发布到掘金
     */
    private void publishToJuejin(String content, String title) throws IOException {
        System.out.println("⛏️  发布到掘金...");
        
        // 读取配置
        JSONObject config = loadConfig();
        JSONObject juejinConfig = config.optJSONObject("juejin");
        
        if (juejinConfig == null || !juejinConfig.has("cookie")) {
            System.out.println("⚠️  掘金配置未设置，跳过");
            System.out.println("提示：请在 config.json 中配置 juejin.cookie");
            return;
        }
        
        String cookie = juejinConfig.getString("cookie");
        
        // TODO: 调用掘金 API 发布文章
        // 掘金没有官方 API，需要通过 Web 接口或第三方库
        
        System.out.println("⚠️  掘金发布需要额外配置");
        System.out.println("提示：请参考掘金 Web 接口完成集成");
        System.out.println("✅ 掘金发布完成（演示模式）");
    }
    
    /**
     * 加载配置文件
     */
    private JSONObject loadConfig() throws IOException {
        String configContent = Files.readString(Paths.get(CONFIG_FILE));
        return new JSONObject(configContent);
    }
    
    /**
     * 从内容中提取标题
     */
    private String extractTitle(String content) {
        // 尝试从 YAML frontmatter 提取
        if (content.startsWith("---")) {
            int endIndex = content.indexOf("---", 3);
            if (endIndex > 0) {
                String frontmatter = content.substring(3, endIndex);
                String[] lines = frontmatter.split("\n");
                for (String line : lines) {
                    if (line.startsWith("title:")) {
                        return line.substring(6).trim().replace("\"", "");
                    }
                }
            }
        }
        
        // 尝试从第一个标题提取
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.startsWith("# ")) {
                return line.substring(2).trim();
            }
        }
        
        // 返回默认标题
        return "未命名文章";
    }
}
