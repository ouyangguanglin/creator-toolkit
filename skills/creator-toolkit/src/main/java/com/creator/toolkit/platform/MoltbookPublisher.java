package com.creator.toolkit.platform;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Moltbook 发布服务
 */
@Service
public class MoltbookPublisher {

    private static final String API_BASE_URL = "https://www.moltbook.com/api/v1";
    
    private final String apiKey;
    private final OkHttpClient client;

    public MoltbookPublisher(@Value("${platforms.moltbook.api-key:}") String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
    }

    /**
     * 发布帖子到 Moltbook
     * 
     * @param title 标题
     * @param content 内容（Markdown 格式）
     * @param submolt 子板块名称（social/tech/learning 等）
     * @return 帖子 ID
     */
    public String publish(String title, String content, String submolt) throws IOException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", title);
        requestBody.put("content", content);
        requestBody.put("submolt", submolt);

        RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
            .url(API_BASE_URL + "/posts")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + apiKey)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("发布失败：" + response.code() + " - " + response.message());
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            if (jsonResponse.getBoolean("success")) {
                JSONObject post = jsonResponse.getJSONObject("post");
                String postId = post.getString("id");
                System.out.println("✅ 发布成功！帖子 ID: " + postId);
                System.out.println("链接：https://www.moltbook.com/posts/" + postId);
                return postId;
            } else {
                throw new IOException("发布失败：" + jsonResponse.getString("message"));
            }
        }
    }

    /**
     * 发布到 social 板块
     */
    public String publishSocial(String title, String content) throws IOException {
        return publish(title, content, "social");
    }

    /**
     * 发布到 tech 板块
     */
    public String publishTech(String title, String content) throws IOException {
        return publish(title, content, "tech");
    }

    /**
     * 发布到 learning 板块
     */
    public String publishLearning(String title, String content) throws IOException {
        return publish(title, content, "learning");
    }
}
