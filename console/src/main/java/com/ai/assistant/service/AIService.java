package com.ai.assistant.service;

import com.ai.assistant.dto.ChatRequest;
import com.ai.assistant.dto.OpenAIRequest;
import com.ai.assistant.dto.OpenAIResponse;
import com.ai.assistant.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {
    
    private final ObjectMapper objectMapper;
    
    @Value("${ai.default.base-url}")
    private String defaultBaseUrl;
    
    @Value("${ai.default.model}")
    private String defaultModel;
    
    @Value("${ai.default.temperature}")
    private Double defaultTemperature;
    
    @Value("${ai.default.max-tokens}")
    private Integer defaultMaxTokens;
    
    @Value("${ai.default.stream}")
    private Boolean defaultStream;
    
    public String chat(List<ChatMessage> history, ChatRequest.AIConfig config) {
        try {
            // 构建请求
            OpenAIRequest request = buildRequest(history, config);
            
            // 发送HTTP请求
            HttpResponse<String> response = sendRequest(request, config);
            
            // 解析响应
            return parseResponse(response.body());
            
        } catch (Exception e) {
            log.error("AI service error", e);
            return "抱歉，AI服务暂时不可用，请稍后再试。";
        }
    }
    
    private OpenAIRequest buildRequest(List<ChatMessage> history, ChatRequest.AIConfig config) {
        OpenAIRequest request = new OpenAIRequest();
        
        // 设置模型参数
        request.setModel(config != null && config.getModel() != null ? config.getModel() : defaultModel);
        request.setTemperature(config != null && config.getTemperature() != null ? config.getTemperature() : defaultTemperature);
        request.setMax_tokens(config != null && config.getMaxTokens() != null ? config.getMaxTokens() : defaultMaxTokens);
        request.setStream(false); // 暂时不支持流式输出
        
        // 转换消息历史
        List<OpenAIRequest.Message> messages = history.stream()
            .map(msg -> new OpenAIRequest.Message(
                msg.getRole().name().toLowerCase(),
                msg.getContent()
            ))
            .collect(Collectors.toList());
        
        request.setMessages(messages);
        
        return request;
    }
    
    private HttpResponse<String> sendRequest(OpenAIRequest request, ChatRequest.AIConfig config) throws Exception {
        String baseUrl = config != null && config.getBaseUrl() != null ? config.getBaseUrl() : defaultBaseUrl;
        String apiKey = config != null && config.getApiKey() != null ? config.getApiKey() : "";
        
        HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl))
            .timeout(Duration.ofMinutes(2))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(request)));
        
        // 如果有API密钥，添加认证头
        if (apiKey != null && !apiKey.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }
        
        return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }
    
    private String parseResponse(String responseBody) throws Exception {
        log.debug("AI service response: {}", responseBody);
        
        OpenAIResponse response = objectMapper.readValue(responseBody, OpenAIResponse.class);
        
        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
            OpenAIResponse.Choice choice = response.getChoices().get(0);
            if (choice.getMessage() != null && choice.getMessage().getContent() != null) {
                return choice.getMessage().getContent().trim();
            }
        }
        
        return "抱歉，AI服务返回了无效的响应。";
    }
}