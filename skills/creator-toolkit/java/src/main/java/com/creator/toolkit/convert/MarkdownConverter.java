package com.creator.toolkit.convert;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown 转换器
 * 📝 转换为微信公众号、知乎、HTML 等格式
 */
public class MarkdownConverter {
    
    private static final Logger logger = LoggerFactory.getLogger(MarkdownConverter.class);
    
    private String targetPlatform = "html";
    private boolean extractCover = false;
    private String coverImage = null;
    
    // Markdown 解析器配置
    private final Parser parser;
    private final HtmlRenderer renderer;
    
    public MarkdownConverter() {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(
            TablesExtension.create(),
            StrikethroughExtension.create()
        ));
        
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }
    
    public void setTargetPlatform(String targetPlatform) {
        this.targetPlatform = targetPlatform.toLowerCase();
    }
    
    public void setExtractCover(boolean extractCover) {
        this.extractCover = extractCover;
    }
    
    /**
     * 转换 Markdown 文件
     */
    public String convert(String inputFile) throws IOException {
        Path inputPath = Paths.get(inputFile);
        
        if (!Files.exists(inputPath)) {
            throw new IOException("文件不存在：" + inputFile);
        }
        
        System.out.println("📄 输入文件：" + inputFile);
        System.out.println("🎯 目标平台：" + targetPlatform);
        
        // 读取 Markdown 内容
        String markdown = Files.readString(inputPath);
        
        // 提取封面图（如果请求）
        if (extractCover) {
            coverImage = extractFirstImage(markdown);
            if (coverImage != null) {
                System.out.println("🖼️  封面图：" + coverImage);
            } else {
                System.out.println("⚠️  未找到封面图");
            }
        }
        
        // 转换为 HTML
        String html = renderer.render(parser.parse(markdown));
        
        // 根据目标平台生成输出
        String outputFile = switch (targetPlatform) {
            case "wechat" -> generateWechatHtml(inputFile, html);
            case "zhihu" -> generateZhihuMarkdown(inputFile, markdown);
            case "html" -> generateStandardHtml(inputFile, html);
            default -> throw new IOException("不支持的目标平台：" + targetPlatform);
        };
        
        return outputFile;
    }
    
    /**
     * 提取第一张图片
     */
    private String extractFirstImage(String markdown) {
        Pattern pattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
        Matcher matcher = pattern.matcher(markdown);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return null;
    }
    
    /**
     * 生成微信公众号 HTML
     */
    private String generateWechatHtml(String inputFile, String html) throws IOException {
        String fileName = getFileNameWithoutExt(inputFile);
        String outputFile = inputFile.replace(".md", "-wechat.html");
        
        String wechatHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
                        line-height: 1.75;
                        color: #333;
                        max-width: 677px;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #fff;
                    }
                    h1, h2, h3, h4, h5, h6 {
                        color: #1a1a1a;
                        margin-top: 24px;
                        margin-bottom: 16px;
                        font-weight: 600;
                        line-height: 1.4;
                    }
                    h1 {
                        font-size: 24px;
                        border-bottom: 1px solid #eaecef;
                        padding-bottom: 0.3em;
                    }
                    h2 {
                        font-size: 20px;
                        border-bottom: 1px solid #eaecef;
                        padding-bottom: 0.3em;
                    }
                    h3 { font-size: 16px; }
                    p { margin: 16px 0; }
                    code {
                        background-color: #f6f8fa;
                        padding: 0.2em 0.4em;
                        border-radius: 3px;
                        font-family: SFMono-Regular, Consolas, "Liberation Mono", Menlo, monospace;
                        font-size: 85%;
                    }
                    pre {
                        background-color: #f6f8fa;
                        padding: 16px;
                        border-radius: 6px;
                        overflow: auto;
                        border: 1px solid #e1e4e8;
                    }
                    pre code {
                        background: none;
                        padding: 0;
                        font-size: 100%;
                    }
                    blockquote {
                        border-left: 4px solid #dfe2e5;
                        padding-left: 16px;
                        color: #6a737d;
                        margin: 16px 0;
                    }
                    img {
                        max-width: 100%;
                        height: auto;
                        display: block;
                        margin: 16px auto;
                        border-radius: 4px;
                    }
                    a { color: #0366d6; text-decoration: none; }
                    a:hover { text-decoration: underline; }
                    ul, ol { padding-left: 2em; }
                    hr {
                        height: 0.25em;
                        padding: 0;
                        margin: 24px 0;
                        background-color: #e1e4e8;
                        border: 0;
                    }
                    table {
                        border-collapse: collapse;
                        width: 100%;
                        margin: 16px 0;
                    }
                    th, td {
                        border: 1px solid #dfe2e5;
                        padding: 8px 12px;
                        text-align: left;
                    }
                    th {
                        background-color: #f6f8fa;
                        font-weight: 600;
                    }
                    tr:nth-child(even) {
                        background-color: #f6f8fa;
                    }
                </style>
            </head>
            <body>
            """ + html + """
            </body>
            </html>
            """;
        
        Files.writeString(Paths.get(outputFile), wechatHtml);
        System.out.println("✅ 微信公众号格式：" + outputFile);
        
        return outputFile;
    }
    
    /**
     * 生成知乎 Markdown（优化版）
     */
    private String generateZhihuMarkdown(String inputFile, String markdown) throws IOException {
        String fileName = getFileNameWithoutExt(inputFile);
        String outputFile = inputFile.replace(".md", "-zhihu.md");
        
        // 知乎支持 Markdown，直接复制并优化
        // 可以在这里添加知乎特定的优化，比如添加话题标签等
        
        Files.writeString(Paths.get(outputFile), markdown);
        System.out.println("✅ 知乎格式：" + outputFile);
        
        return outputFile;
    }
    
    /**
     * 生成标准 HTML
     */
    private String generateStandardHtml(String inputFile, String html) throws IOException {
        String fileName = getFileNameWithoutExt(inputFile);
        String outputFile = inputFile.replace(".md", ".html");
        
        String standardHtml = """
            <!DOCTYPE html>
            <html lang="zh-CN">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>""" + fileName + """</title>
                <style>
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 800px;
                        margin: 0 auto;
                        padding: 20px;
                        background: #f5f5f5;
                    }
                    .container {
                        background: #fff;
                        padding: 40px;
                        border-radius: 8px;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                    }
                    h1, h2, h3 { color: #1a1a1a; }
                    code {
                        background: #f6f8fa;
                        padding: 2px 6px;
                        border-radius: 3px;
                    }
                    pre {
                        background: #f6f8fa;
                        padding: 16px;
                        border-radius: 6px;
                        overflow: auto;
                    }
                    img { max-width: 100%; height: auto; }
                </style>
            </head>
            <body>
                <div class="container">
            """ + html + """
                </div>
            </body>
            </html>
            """;
        
        Files.writeString(Paths.get(outputFile), standardHtml);
        System.out.println("✅ HTML 格式：" + outputFile);
        
        return outputFile;
    }
    
    private String getFileNameWithoutExt(String fileName) {
        Path path = Paths.get(fileName);
        String name = path.getFileName().toString();
        return name.substring(0, name.lastIndexOf('.'));
    }
}
