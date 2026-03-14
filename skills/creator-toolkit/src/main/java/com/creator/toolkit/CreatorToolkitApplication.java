package com.creator.toolkit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * CreatorToolkit - 创作者工具箱
 * 
 * 个人创作者的一站式内容处理与发布工具
 */
@SpringBootApplication
public class CreatorToolkitApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreatorToolkitApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            System.out.println("🦞 创作者工具箱已启动！");
            System.out.println("用法：java -jar creator-toolkit.jar [command] [options]");
            System.out.println();
            System.out.println("可用命令:");
            System.out.println("  image compress  - 批量压缩图片");
            System.out.println("  image convert   - 批量转换图片格式");
            System.out.println("  image watermark - 批量添加水印");
            System.out.println("  convert md      - Markdown 格式转换");
            System.out.println("  publish         - 发布到多平台");
            System.out.println();
            System.out.println("使用 --help 查看详细用法");
        };
    }
}
