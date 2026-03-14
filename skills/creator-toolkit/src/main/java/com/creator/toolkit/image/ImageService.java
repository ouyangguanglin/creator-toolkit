package com.creator.toolkit.image;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理服务
 * 
 * 功能：
 * - 批量压缩
 * - 格式转换
 * - 添加水印
 */
@Service
public class ImageService {

    /**
     * 批量压缩图片
     * 
     * @param inputDir 输入目录
     * @param outputDir 输出目录
     * @param quality 质量 (0.0-1.0)
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     */
    public void compressImages(String inputDir, String outputDir, 
                               float quality, int maxWidth, int maxHeight) throws IOException {
        File inputFolder = new File(inputDir);
        File outputFolder = new File(outputDir);
        
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        File[] images = inputFolder.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".png") || 
            name.toLowerCase().endsWith(".jpg") || 
            name.toLowerCase().endsWith(".jpeg") ||
            name.toLowerCase().endsWith(".webp")
        );

        if (images == null || images.length == 0) {
            System.out.println("未找到图片文件");
            return;
        }

        int processed = 0;
        for (File image : images) {
            try {
                String outputName = outputDir + File.separator + image.getName();
                
                Thumbnails.of(image)
                    .size(maxWidth, maxHeight)
                    .outputQuality(quality)
                    .toFile(outputName);
                
                processed++;
                System.out.println("已压缩：" + image.getName());
            } catch (Exception e) {
                System.err.println("压缩失败：" + image.getName() + " - " + e.getMessage());
            }
        }

        System.out.println("完成！共处理 " + processed + "/" + images.length + " 张图片");
    }

    /**
     * 批量转换图片格式
     */
    public void convertImages(String inputDir, String outputDir, String targetFormat) throws IOException {
        File inputFolder = new File(inputDir);
        File outputFolder = new File(outputDir);
        
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        File[] images = inputFolder.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".png") || 
            name.toLowerCase().endsWith(".jpg") || 
            name.toLowerCase().endsWith(".jpeg")
        );

        if (images == null || images.length == 0) {
            System.out.println("未找到图片文件");
            return;
        }

        int processed = 0;
        for (File image : images) {
            try {
                BufferedImage originalImage = ImageIO.read(image);
                String baseName = image.getName().substring(0, image.getName().lastIndexOf("."));
                String outputName = outputDir + File.separator + baseName + "." + targetFormat.toLowerCase();
                
                ImageIO.write(originalImage, targetFormat.toLowerCase(), new File(outputName));
                
                processed++;
                System.out.println("已转换：" + image.getName() + " -> " + baseName + "." + targetFormat);
            } catch (Exception e) {
                System.err.println("转换失败：" + image.getName() + " - " + e.getMessage());
            }
        }

        System.out.println("完成！共处理 " + processed + "/" + images.length + " 张图片");
    }
}
