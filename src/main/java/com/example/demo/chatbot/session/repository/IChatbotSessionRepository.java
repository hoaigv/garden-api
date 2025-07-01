package com.example.demo.chatbot.session.repository;

import com.example.demo.chatbot.session.model.ChatbotSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ChatbotSessionEntity
 */
@Repository
public interface IChatbotSessionRepository extends JpaRepository<ChatbotSessionEntity, String> {

    /**
     * Find a session by its sessionId
     *
     * @param id the unique session identifier
     * @return Optional of ChatbotSessionEntity
     */
    Optional<ChatbotSessionEntity> findAllById(String id);

    /**
     * Find all sessions for a given user ID
     *
     * @param userId the user's ID
     * @return list of ChatbotSessionEntity
     */
    Optional<List<ChatbotSessionEntity>> findAllByUser_Id(String userId);
}
