package com.ai.assistant.repository;

import com.ai.assistant.entity.ChatMessage;
import com.ai.assistant.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findBySessionOrderByCreatedAtAsc(ChatSession session);
    
    void deleteBySession(ChatSession session);
}