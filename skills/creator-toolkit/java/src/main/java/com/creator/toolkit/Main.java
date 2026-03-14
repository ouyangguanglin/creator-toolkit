package com.creator.toolkit;

import com.creator.toolkit.core.Command;
import com.creator.toolkit.image.ImageCompressor;
import com.creator.toolkit.convert.MarkdownConverter;
import com.creator.toolkit.publish.MultiPlatformPublisher;
import org.apache.commons.cli.*;

/**
 * Creator Toolkit - 主入口
 * 🧰 个人创作者的效率工具箱
 */
public class Main {

    public static void main(String[] args) {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            if (cmd.hasOption("help") || args.length == 0) {
                printHelp(options);
                return;
            }
            
            String command = cmd.getArgs()[0];
            String[] cmdArgs = new String[args.length - 1];
            System.arraycopy(args, 1, cmdArgs, 0, cmdArgs.length);
            
            switch (command) {
                case "compress" -> handleCompress(cmd, cmdArgs);
                case "convert" -> handleConvert(cmd, cmdArgs);
                case "publish" -> handlePublish(cmd, cmdArgs);
                case "version" -> printVersion();
                default -> {
                    System.err.println("未知命令：" + command);
                    printHelp(options);
                    System.exit(1);
                }
            }
            
        } catch (ParseException e) {
            System.err.println("参数解析错误：" + e.getMessage());
            printHelp(options);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("执行错误：" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static Options createOptions() {
        Options options = new Options();
        
        // 全局选项
        options.addOption("h", "help", false, "显示帮助信息");
        options.addOption("v", "version", false, "显示版本号");
        
        // compress 选项
        options.addOption("q", "quality", true, "图片质量 (1-100, 默认：85)");
        options.addOption("f", "format", true, "输出格式 (jpg/png/webp, 默认：webp)");
        options.addOption("o", "output", true, "输出目录");
        options.addOption("r", "resize", true, "调整尺寸 (WxH)");
        
        // convert 选项
        options.addOption("t", "target", true, "目标平台 (wechat/zhihu/html)");
        options.addOption("c", "cover", true, "封面图路径");
        options.addOption("extract-cover", false, "自动提取封面图");
        
        // publish 选项
        options.addOption("p", "platforms", true, "发布平台，逗号分隔");
        options.addOption("s", "schedule", true, "定时发布时间 (YYYY-MM-DD HH:mm)");
        options.addOption("tags", true, "标签，逗号分隔");
        options.addOption("title", true, "文章标题");
        
        return options;
    }
    
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        
        System.out.println("""
            🧰 Creator Toolkit - 个人创作者的效率工具箱
            
            用法：java -jar creator-toolkit.jar <命令> [选项]
            
            命令:
              compress    批量压缩图片
              convert     格式转换
              publish     发布文章到多平台
              version     显示版本号
              help        显示帮助信息
            
            示例:
              java -jar creator-toolkit.jar compress ./images --quality 80
              java -jar creator-toolkit.jar convert ./article.md --target wechat
              java -jar creator-toolkit.jar publish ./article.md --platforms wechat,zhihu
            """);
        
        formatter.printOptions("选项:", "选项:", options, 100, 4);
    }
    
    private static void printVersion() {
        System.out.println("Creator Toolkit v0.1.0");
        System.out.println("Java " + System.getProperty("java.version"));
    }
    
    private static void handleCompress(CommandLine cmd, String[] args) throws Exception {
        System.out.println("🖼️  图片压缩模式");
        
        String quality = cmd.getOptionValue("quality", "85");
        String format = cmd.getOptionValue("format", "webp");
        String output = cmd.getOptionValue("output", null);
        String resize = cmd.getOptionValue("resize", null);
        
        String[] remaining = cmd.getArgs();
        if (remaining.length < 1) {
            System.err.println("请指定输入目录");
            System.exit(1);
        }
        
        String inputDir = remaining[0];
        
        ImageCompressor compressor = new ImageCompressor();
        compressor.setQuality(Integer.parseInt(quality));
        compressor.setFormat(format);
        compressor.setOutputDir(output);
        
        if (resize != null) {
            String[] dims = resize.split("x");
            if (dims.length == 2) {
                compressor.setWidth(Integer.parseInt(dims[0]));
                compressor.setHeight(Integer.parseInt(dims[1]));
            }
        }
        
        int count = compressor.compressDirectory(inputDir);
        System.out.println("✅ 完成！处理了 " + count + " 张图片");
    }
    
    private static void handleConvert(CommandLine cmd, String[] args) throws Exception {
        System.out.println("📝 格式转换模式");
        
        String target = cmd.getOptionValue("target", "html");
        boolean extractCover = cmd.hasOption("extract-cover");
        
        String[] remaining = cmd.getArgs();
        if (remaining.length < 1) {
            System.err.println("请指定输入文件");
            System.exit(1);
        }
        
        String inputFile = remaining[0];
        
        MarkdownConverter converter = new MarkdownConverter();
        converter.setTargetPlatform(target);
        converter.setExtractCover(extractCover);
        
        String outputFile = converter.convert(inputFile);
        System.out.println("✅ 转换完成：" + outputFile);
    }
    
    private static void handlePublish(CommandLine cmd, String[] args) throws Exception {
        System.out.println("📤 多平台发布模式");
        
        String platforms = cmd.getOptionValue("platforms", "");
        String schedule = cmd.getOptionValue("schedule", null);
        String tags = cmd.getOptionValue("tags", null);
        String title = cmd.getOptionValue("title", null);
        
        String[] remaining = cmd.getArgs();
        if (remaining.length < 1) {
            System.err.println("请指定文章文件");
            System.exit(1);
        }
        
        String inputFile = remaining[0];
        
        MultiPlatformPublisher publisher = new MultiPlatformPublisher();
        publisher.setPlatforms(platforms.split(","));
        publisher.setTitle(title);
        publisher.setTags(tags != null ? tags.split(",") : new String[0]);
        
        if (schedule != null) {
            publisher.setSchedule(schedule);
        }
        
        publisher.publish(inputFile);
        System.out.println("✅ 发布完成！");
    }
}
