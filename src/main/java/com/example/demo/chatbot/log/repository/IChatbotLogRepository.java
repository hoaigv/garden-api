package com.example.demo.chatbot.log.repository;

import com.example.demo.chatbot.log.model.ChatbotLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for ChatbotSessionEntity
 */
@Repository
public interface IChatbotLogRepository extends JpaRepository<ChatbotLogEntity, String>, JpaSpecificationExecutor<ChatbotLogEntity> {


}
