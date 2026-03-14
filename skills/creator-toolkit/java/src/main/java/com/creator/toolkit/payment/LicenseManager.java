package com.creator.toolkit.payment;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 支付和授权管理
 * 💰 处理免费版/Pro 版验证
 */
public class LicenseManager {
    
    private static final Logger logger = LoggerFactory.getLogger(LicenseManager.class);
    
    private static final String LICENSE_FILE = "license.json";
    private static final String CONFIG_FILE = "config.json";
    
    private LicenseType licenseType = LicenseType.FREE;
    private String licenseKey;
    private LocalDateTime expireDate;
    
    // 免费版限制
    public static final int FREE_BATCH_LIMIT = 10;
    public static final int FREE_PLATFORM_LIMIT = 2;
    
    public enum LicenseType {
        FREE("免费版", 0),
        PRO_MONTHLY("Pro 月付", 29),
        PRO_YEARLY("Pro 年付", 299),
        PRO_LIFETIME("Pro 终身", 599);
        
        private final String displayName;
        private final int price;
        
        LicenseType(String displayName, int price) {
            this.displayName = displayName;
            this.price = price;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public int getPrice() {
            return price;
        }
    }
    
    /**
     * 初始化授权管理器
     */
    public void init() {
        try {
            loadLicense();
        } catch (IOException e) {
            logger.warn("加载授权文件失败，使用免费版", e);
            licenseType = LicenseType.FREE;
        }
    }
    
    /**
     * 加载授权信息
     */
    private void loadLicense() throws IOException {
        if (!Files.exists(Paths.get(LICENSE_FILE))) {
            logger.info("授权文件不存在，使用免费版");
            licenseType = LicenseType.FREE;
            return;
        }
        
        String content = Files.readString(Paths.get(LICENSE_FILE));
        JSONObject json = new JSONObject(content);
        
        String typeStr = json.optString("type", "FREE");
        try {
            licenseType = LicenseType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            licenseType = LicenseType.FREE;
        }
        
        licenseKey = json.optString("licenseKey", null);
        
        if (json.has("expireDate")) {
            expireDate = LocalDateTime.parse(
                json.getString("expireDate"), 
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            );
            
            // 检查是否过期
            if (LocalDateTime.now().isAfter(expireDate)) {
                logger.warn("授权已过期，降级为免费版");
                licenseType = LicenseType.FREE;
            }
        }
        
        logger.info("授权类型：" + licenseType.getDisplayName());
    }
    
    /**
     * 保存授权信息
     */
    public void saveLicense() throws IOException {
        JSONObject json = new JSONObject();
        json.put("type", licenseType.name());
        
        if (licenseKey != null) {
            json.put("licenseKey", licenseKey);
        }
        
        if (expireDate != null) {
            json.put("expireDate", expireDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        
        Files.writeString(Paths.get(LICENSE_FILE), json.toString(2));
    }
    
    /**
     * 验证授权码
     */
    public boolean verifyLicenseKey(String licenseKey) {
        // TODO: 实现授权码验证逻辑
        // 这里可以连接验证服务器或本地验证
        
        logger.info("验证授权码：" + licenseKey);
        
        // 演示模式：简单验证格式
        if (licenseKey != null && licenseKey.startsWith("PRO-")) {
            this.licenseKey = licenseKey;
            this.licenseType = LicenseType.PRO_LIFETIME;
            this.expireDate = null; // 终身版不过期
            
            try {
                saveLicense();
            } catch (IOException e) {
                logger.error("保存授权失败", e);
            }
            
            return true;
        }
        
        return false;
    }
    
    /**
     * 检查是否可以使用某功能
     */
    public boolean canUse(Feature feature, int currentValue) {
        if (licenseType == LicenseType.FREE) {
            return switch (feature) {
                case BATCH_PROCESS -> currentValue < FREE_BATCH_LIMIT;
                case MULTI_PLATFORM -> currentValue < FREE_PLATFORM_LIMIT;
                case SCHEDULE_PUBLISH -> false;
                case DATA_ANALYTICS -> false;
                case PRIORITY_SUPPORT -> false;
            };
        }
        
        // Pro 版无限制
        return true;
    }
    
    /**
     * 获取功能限制信息
     */
    public String getLimitMessage(Feature feature) {
        if (licenseType == LicenseType.FREE) {
            return switch (feature) {
                case BATCH_PROCESS -> "⚠️  免费版限制：最多处理 " + FREE_BATCH_LIMIT + " 张/次\n升级到 Pro 解锁无限批量处理：¥29/月";
                case MULTI_PLATFORM -> "⚠️  免费版限制：最多发布 " + FREE_PLATFORM_LIMIT + " 个平台\n升级到 Pro 解锁无限平台发布：¥29/月";
                case SCHEDULE_PUBLISH -> "⚠️  定时发布是 Pro 功能\n升级到 Pro：¥29/月";
                case DATA_ANALYTICS -> "⚠️  数据看板是 Pro 功能\n升级到 Pro：¥29/月";
                case PRIORITY_SUPPORT -> "⚠️  优先技术支持是 Pro 功能\n升级到 Pro：¥29/月";
            };
        }
        
        return null;
    }
    
    public enum Feature {
        BATCH_PROCESS,      // 批量处理
        MULTI_PLATFORM,     // 多平台发布
        SCHEDULE_PUBLISH,   // 定时发布
        DATA_ANALYTICS,     // 数据分析
        PRIORITY_SUPPORT    // 优先支持
    }
    
    public LicenseType getLicenseType() {
        return licenseType;
    }
    
    public boolean isPro() {
        return licenseType != LicenseType.FREE;
    }
    
    public LocalDateTime getExpireDate() {
        return expireDate;
    }
}
