package com.example.demo.chatbot.log.repository;

import com.example.demo.chatbot.log.model.ChatbotLogEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for filtering ChatbotLogEntity queries.
 */
public class ChatLogSpecification {

    /**
     * Filter by log ID (String).
     */
    public static Specification<ChatbotLogEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    /**
     * Filter by parent session ID (String).
     */
    public static Specification<ChatbotLogEntity> hasSessionId(String sessionId) {
        return (root, query, cb) -> {
            Join<?, ?> sessionJoin = root.join("session", JoinType.INNER);
            return cb.equal(sessionJoin.get("id"), sessionId);
        };
    }


    /**
     * Filter logs containing a substring in the user's message.
     */
    public static Specification<ChatbotLogEntity> userMessageContains(String keyword) {
        return (root, query, cb) -> cb.like(
                cb.lower(root.get("userMessage")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    /**
     * Filter logs containing a substring in the bot's response.
     */
    public static Specification<ChatbotLogEntity> botResponseContains(String keyword) {
        return (root, query, cb) -> cb.like(
                cb.lower(root.get("botResponse")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    /**
     * Filter logs created after or at the given timestamp.
     */
    public static Specification<ChatbotLogEntity> createdAfter(java.time.Instant from) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    /**
     * Filter logs created before or at the given timestamp.
     */
    public static Specification<ChatbotLogEntity> createdBefore(java.time.Instant to) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }

    /**
     * Build a combined specification with optional filters.
     */
    public static Specification<ChatbotLogEntity> build(
            String id,
            String sessionId,
            String userMessageKeyword,
            String botResponseKeyword,
            java.time.Instant createdAfter,
            java.time.Instant createdBefore
    ) {
        Specification<ChatbotLogEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (sessionId != null && !sessionId.isBlank()) {
            spec = spec.and(hasSessionId(sessionId));
        }
        if (userMessageKeyword != null && !userMessageKeyword.isBlank()) {
            spec = spec.and(userMessageContains(userMessageKeyword));
        }
        if (botResponseKeyword != null && !botResponseKeyword.isBlank()) {
            spec = spec.and(botResponseContains(botResponseKeyword));
        }
        if (createdAfter != null) {
            spec = spec.and(createdAfter(createdAfter));
        }
        if (createdBefore != null) {
            spec = spec.and(createdBefore(createdBefore));
        }

        return spec;
    }
}
