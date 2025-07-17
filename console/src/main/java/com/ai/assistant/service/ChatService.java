package com.ai.assistant.service;

import com.ai.assistant.dto.ChatRequest;
import com.ai.assistant.dto.ChatResponse;
import com.ai.assistant.entity.ChatMessage;
import com.ai.assistant.entity.ChatSession;
import com.ai.assistant.repository.ChatMessageRepository;
import com.ai.assistant.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository messageRepository;
    private final AIService aiService;
    
    @Transactional
    public ChatResponse sendMessage(ChatRequest request) {
        // 获取或创建会话
        ChatSession session = getOrCreateSession(request.getSessionId());
        
        // 保存用户消息
        ChatMessage userMessage = new ChatMessage();
        userMessage.setMessageId(UUID.randomUUID().toString());
        userMessage.setContent(request.getMessage());
        userMessage.setRole(ChatMessage.Role.USER);
        userMessage.setSession(session);
        messageRepository.save(userMessage);
        
        // 获取会话历史
        List<ChatMessage> history = messageRepository.findBySessionOrderByCreatedAtAsc(session);
        
        // 调用AI服务
        String aiResponse = aiService.chat(history, request.getConfig());
        
        // 保存AI回复
        ChatMessage assistantMessage = new ChatMessage();
        String messageId = UUID.randomUUID().toString();
        assistantMessage.setMessageId(messageId);
        assistantMessage.setContent(aiResponse);
        assistantMessage.setRole(ChatMessage.Role.ASSISTANT);
        assistantMessage.setSession(session);
        messageRepository.save(assistantMessage);
        
        // 更新会话标题（如果是第一次对话）
        if (session.getTitle().equals("新对话") && history.size() <= 2) {
            String title = request.getMessage().length() > 20 
                ? request.getMessage().substring(0, 20) + "..." 
                : request.getMessage();
            session.setTitle(title);
            sessionRepository.save(session);
        }
        
        return new ChatResponse(messageId, aiResponse, session.getSessionId());
    }
    
    public List<ChatSession> getAllSessions() {
        return sessionRepository.findAllByOrderByUpdatedAtDesc();
    }
    
    @Transactional
    public void deleteSession(String sessionId) {
        ChatSession session = sessionRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
        
        messageRepository.deleteBySession(session);
        sessionRepository.delete(session);
    }
    
    private ChatSession getOrCreateSession(String sessionId) {
        if (sessionId != null) {
            return sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        }
        
        // 创建新会话
        ChatSession session = new ChatSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setTitle("新对话");
        return sessionRepository.save(session);
    }
}