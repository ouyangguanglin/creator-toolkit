package com.creator.toolkit.web;

import com.creator.toolkit.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片处理 Web 控制器
 */
@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    private final String uploadDir = System.getProperty("java.io.tmpdir") + "/creator-toolkit/uploads";
    private final String outputDir = System.getProperty("java.io.tmpdir") + "/creator-toolkit/output";

    /**
     * 显示图片压缩页面
     */
    @GetMapping("/compress")
    public String compressPage() {
        return "compress";
    }

    /**
     * 处理图片压缩
     */
    @PostMapping("/compress")
    @ResponseBody
    public List<String> compressImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "quality", defaultValue = "0.8") float quality,
            Model model) throws IOException {
        
        // 创建目录
        new File(uploadDir).mkdirs();
        new File(outputDir).mkdirs();

        List<String> resultFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            // 保存上传文件
            Path uploadPath = Path.of(uploadDir, file.getOriginalFilename());
            Files.write(uploadPath, file.getBytes());

            // 压缩图片
            String outputFilename = "compressed_" + file.getOriginalFilename();
            Path outputPath = Path.of(outputDir, outputFilename);
            
            imageService.compressImage(
                uploadPath.toString(),
                outputPath.toString(),
                quality,
                1920,
                1080
            );

            resultFiles.add(outputFilename);
        }

        return resultFiles;
    }

    /**
     * 下载压缩后的图片
     */
    @GetMapping("/download/{filename}")
    @ResponseBody
    public byte[] downloadFile(@PathVariable String filename) throws IOException {
        Path filePath = Path.of(outputDir, filename);
        return Files.readAllBytes(filePath);
    }
}
