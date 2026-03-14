package com.creator.toolkit.image;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片压缩器
 * 🖼️ 批量压缩、转换格式、调整尺寸
 */
public class ImageCompressor {
    
    private static final Logger logger = LoggerFactory.getLogger(ImageCompressor.class);
    
    private int quality = 85;
    private String format = "webp";
    private String outputDir;
    private Integer width;
    private Integer height;
    private int processedCount = 0;
    private int successCount = 0;
    private int failCount = 0;
    private long totalSizeBefore = 0;
    private long totalSizeAfter = 0;
    
    // 免费版限制
    private static final int FREE_BATCH_LIMIT = 10;
    
    public ImageCompressor() {
        // 注册 WebP 支持（需要额外库）
        // ImageIO.scanForPlugins();
    }
    
    public void setQuality(int quality) {
        this.quality = Math.max(1, Math.min(100, quality));
    }
    
    public void setFormat(String format) {
        this.format = format.toLowerCase();
    }
    
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
    
    public void setWidth(Integer width) {
        this.width = width;
    }
    
    public void setHeight(Integer height) {
        this.height = height;
    }
    
    /**
     * 压缩目录中的所有图片
     */
    public int compressDirectory(String inputDir) throws IOException {
        Path inputPath = Paths.get(inputDir);
        
        if (!Files.exists(inputPath)) {
            throw new IOException("目录不存在：" + inputDir);
        }
        
        // 创建输出目录
        if (outputDir == null) {
            outputDir = inputDir + "/compressed";
        }
        Path outputPath = Paths.get(outputDir);
        Files.createDirectories(outputPath);
        
        System.out.println("📁 输入目录：" + inputDir);
        System.out.println("📁 输出目录：" + outputDir);
        System.out.println("⚙️  质量：" + quality);
        System.out.println("⚙️  格式：" + format);
        
        if (width != null && height != null) {
            System.out.println("⚙️  尺寸：" + width + "x" + height);
        }
        
        System.out.println();
        
        // 查找并处理图片
        List<Path> imageFiles = findImageFiles(inputPath);
        
        for (Path imageFile : imageFiles) {
            try {
                compressImage(imageFile, outputPath);
            } catch (Exception e) {
                failCount++;
                logger.error("处理失败：" + imageFile, e);
                System.out.println("  ❌ 失败：" + imageFile.getFileName() + " - " + e.getMessage());
            }
        }
        
        // 打印统计
        printStatistics();
        
        return successCount;
    }
    
    /**
     * 查找目录中的所有图片文件
     */
    private List<Path> findImageFiles(Path directory) throws IOException {
        List<Path> imageFiles = new ArrayList<>();
        
        Files.walkFileTree(directory, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String fileName = file.getFileName().toString().toLowerCase();
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                    fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                    fileName.endsWith(".bmp") || fileName.endsWith(".webp")) {
                    imageFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        
        return imageFiles;
    }
    
    /**
     * 压缩单张图片
     */
    private void compressImage(Path inputFile, Path outputDir) throws IOException {
        processedCount++;
        
        // 检查免费版限制
        if (processedCount > FREE_BATCH_LIMIT) {
            System.out.println("\n⚠️  免费版限制：最多处理 " + FREE_BATCH_LIMIT + " 张图片");
            System.out.println("升级到 Pro 解锁无限批量处理：¥29/月");
            return;
        }
        
        String fileName = inputFile.getFileName().toString();
        String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        String outputFile = outputDir + "/" + nameWithoutExt + "." + format;
        
        // 获取原始大小
        long sizeBefore = Files.size(inputFile);
        totalSizeBefore += sizeBefore;
        
        System.out.println("[" + processedCount + "] 处理：" + fileName);
        
        // 读取图片
        BufferedImage originalImage = ImageIO.read(inputFile.toFile());
        if (originalImage == null) {
            throw new IOException("无法读取图片：" + fileName);
        }
        
        // 调整尺寸（如果指定）
        BufferedImage processedImage = originalImage;
        if (width != null && height != null) {
            processedImage = Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, 
                                         Scalr.Mode.AUTOMATIC, width, height);
        }
        
        // 保存压缩后的图片
        File outputFileObj = new File(outputFile);
        boolean success = ImageIO.write(processedImage, format, outputFileObj);
        
        if (success && outputFileObj.exists()) {
            successCount++;
            
            long sizeAfter = outputFileObj.length();
            totalSizeAfter += sizeAfter;
            
            double ratio = (1.0 - (double) sizeAfter / sizeBefore) * 100;
            System.out.printf("  ✅ 完成 - 压缩率：%.1f%% (%dKB → %dKB)%n", 
                            ratio, sizeBefore / 1024, sizeAfter / 1024);
        } else {
            failCount++;
            System.out.println("  ❌ 失败：无法保存输出文件");
        }
        
        // 释放内存
        originalImage.flush();
        if (processedImage != originalImage) {
            processedImage.flush();
        }
    }
    
    /**
     * 打印统计信息
     */
    private void printStatistics() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("📊 压缩统计");
        System.out.println("=".repeat(40));
        System.out.println("处理图片：" + processedCount + " 张");
        System.out.println("成功：" + successCount);
        System.out.println("失败：" + failCount);
        
        if (totalSizeBefore > 0) {
            double totalRatio = (1.0 - (double) totalSizeAfter / totalSizeBefore) * 100;
            System.out.printf("总大小：%.2f KB → %.2f KB%n", 
                            totalSizeBefore / 1024.0, totalSizeAfter / 1024.0);
            System.out.printf("总压缩率：%.1f%%%n", totalRatio);
        }
    }
    
    /**
     * 压缩单张图片（便捷方法）
     */
    public void compressImage(String inputFile, String outputFile) throws IOException {
        Path inputPath = Paths.get(inputFile);
        Path outputPath = Paths.get(outputFile);
        
        Files.createDirectories(outputPath.getParent());
        
        BufferedImage image = ImageIO.read(inputPath.toFile());
        if (image == null) {
            throw new IOException("无法读取图片：" + inputFile);
        }
        
        if (width != null && height != null) {
            image = Scalr.resize(image, Scalr.Method.AUTOMATIC, width, height);
        }
        
        ImageIO.write(image, format, outputPath.toFile());
        image.flush();
    }
}
