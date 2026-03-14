package com.creator.toolkit.auth;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * 用户认证系统
 * 🔐 用户登录、注册、会话管理
 */
public class UserAuth {
    
    private static final Logger logger = LoggerFactory.getLogger(UserAuth.class);
    
    private static final String USER_FILE = "user.json";
    private static final String SESSION_FILE = "session.json";
    
    private String userId;
    private String email;
    private String sessionToken;
    private LocalDateTime sessionExpire;
    
    /**
     * 注册用户
     */
    public User register(String email, String password) throws IOException {
        // 检查用户是否已存在
        if (userExists(email)) {
            throw new IOException("用户已存在：" + email);
        }
        
        // 创建用户
        User user = new User();
        user.id = generateUserId();
        user.email = email;
        user.passwordHash = hashPassword(password);
        user.createdAt = LocalDateTime.now();
        user.licenseType = "FREE";
        
        // 保存用户
        saveUser(user);
        
        logger.info("用户注册成功：" + email);
        return user;
    }
    
    /**
     * 登录
     */
    public User login(String email, String password) throws IOException, AuthenticationException {
        User user = loadUser(email);
        
        if (user == null) {
            throw new AuthenticationException("用户不存在：" + email);
        }
        
        if (!verifyPassword(password, user.passwordHash)) {
            throw new AuthenticationException("密码错误");
        }
        
        // 创建会话
        createSession(user);
        
        logger.info("用户登录成功：" + email);
        return user;
    }
    
    /**
     * 登出
     */
    public void logout() throws IOException {
        if (Files.exists(Paths.get(SESSION_FILE))) {
            Files.delete(Paths.get(SESSION_FILE));
        }
        
        sessionToken = null;
        sessionExpire = null;
        
        logger.info("用户登出");
    }
    
    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        if (sessionToken == null) {
            // 尝试加载会话
            try {
                loadSession();
            } catch (IOException e) {
                return false;
            }
        }
        
        if (sessionToken == null || sessionExpire == null) {
            return false;
        }
        
        // 检查会话是否过期
        return LocalDateTime.now().isBefore(sessionExpire);
    }
    
    /**
     * 获取当前用户
     */
    public User getCurrentUser() throws IOException {
        if (!isLoggedIn()) {
            return null;
        }
        
        return loadUser(email);
    }
    
    /**
     * 检查用户是否存在
     */
    private boolean userExists(String email) throws IOException {
        return Files.exists(Paths.get(getUserFilePath(email)));
    }
    
    /**
     * 加载用户
     */
    private User loadUser(String email) throws IOException {
        String userFile = getUserFilePath(email);
        
        if (!Files.exists(Paths.get(userFile))) {
            return null;
        }
        
        String content = Files.readString(Paths.get(userFile));
        JSONObject json = new JSONObject(content);
        
        User user = new User();
        user.id = json.getString("id");
        user.email = json.getString("email");
        user.passwordHash = json.getString("passwordHash");
        user.createdAt = LocalDateTime.parse(
            json.getString("createdAt"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        );
        user.licenseType = json.optString("licenseType", "FREE");
        
        return user;
    }
    
    /**
     * 保存用户
     */
    private void saveUser(User user) throws IOException {
        JSONObject json = new JSONObject();
        json.put("id", user.id);
        json.put("email", user.email);
        json.put("passwordHash", user.passwordHash);
        json.put("createdAt", user.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        json.put("licenseType", user.licenseType);
        
        Files.writeString(Paths.get(getUserFilePath(user.email)), json.toString(2));
    }
    
    /**
     * 创建会话
     */
    private void createSession(User user) throws IOException {
        this.userId = user.id;
        this.email = user.email;
        this.sessionToken = generateSessionToken();
        this.sessionExpire = LocalDateTime.now().plusHours(24); // 24 小时会话
        
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("email", email);
        json.put("sessionToken", sessionToken);
        json.put("expire", sessionExpire.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        Files.writeString(Paths.get(SESSION_FILE), json.toString(2));
    }
    
    /**
     * 加载会话
     */
    private void loadSession() throws IOException {
        if (!Files.exists(Paths.get(SESSION_FILE))) {
            return;
        }
        
        String content = Files.readString(Paths.get(SESSION_FILE));
        JSONObject json = new JSONObject(content);
        
        userId = json.getString("userId");
        email = json.getString("email");
        sessionToken = json.getString("sessionToken");
        sessionExpire = LocalDateTime.parse(
            json.getString("expire"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        );
    }
    
    /**
     * 生成用户 ID
     */
    private String generateUserId() {
        return "user_" + System.currentTimeMillis() + "_" + 
               Base64.getUrlEncoder().encodeToString(
                   String.valueOf(System.nanoTime()).getBytes()
               ).substring(0, 8);
    }
    
    /**
     * 生成会话令牌
     */
    private String generateSessionToken() {
        return "sess_" + Base64.getUrlEncoder().encodeToString(
            (System.currentTimeMillis() + "-" + System.nanoTime()).getBytes()
        );
    }
    
    /**
     * 哈希密码
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码哈希失败", e);
        }
    }
    
    /**
     * 验证密码
     */
    private boolean verifyPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }
    
    /**
     * 获取用户文件路径
     */
    private String getUserFilePath(String email) {
        // 使用邮箱作为文件名（替换特殊字符）
        String safeEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
        return "users/" + safeEmail + ".json";
    }
    
    /**
     * 用户类
     */
    public static class User {
        public String id;
        public String email;
        public String passwordHash;
        public LocalDateTime createdAt;
        public String licenseType;
        
        public String getDisplayEmail() {
            // 隐藏部分邮箱
            if (email.contains("@")) {
                String[] parts = email.split("@");
                String name = parts[0];
                if (name.length() > 2) {
                    name = name.charAt(0) + "***" + name.charAt(name.length() - 1);
                }
                return name + "@" + parts[1];
            }
            return email;
        }
    }
    
    /**
     * 认证异常
     */
    public static class AuthenticationException extends Exception {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}
