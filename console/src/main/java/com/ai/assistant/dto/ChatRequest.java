package com.ai.assistant.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private String sessionId;
    private AIConfig config;
    
    @Data
    public static class AIConfig {
        private String baseUrl;
        private String apiKey;
        private String model;
        private Double temperature;
        private Integer maxTokens;
        private Boolean stream;
    }
}