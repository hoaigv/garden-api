package com.example.demo.chatbot.session.repository;

import com.example.demo.chatbot.session.model.ChatbotSessionEntity;
import com.example.demo.common.enums.GardenCondition;
import com.example.demo.user.model.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;

/**
 * Specifications for filtering ChatbotSessionEntity queries.
 */
public class ChatbotSessionSpecification {

    /**
     * Filter by session ID.
     */
    public static Specification<ChatbotSessionEntity> hasId(String id) {
        return (root, query, cb) ->
                cb.equal(root.get("id"), id);
    }

    /**
     * Filter by user ID owning the session.
     */
    public static Specification<ChatbotSessionEntity> hasUserId(String userId) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("id"), userId);
    }
    public static Specification<ChatbotSessionEntity> isNotDelete() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }
    /**
     * Filter by session title (like).
     */
    public static Specification<ChatbotSessionEntity> titleLike(String title) {
        return (root, query, cb) ->
                cb.like(root.get("chatTile"), "%" + title + "%");
    }

    /**
     * Filter by minimum number of logs in the session.
     * Lưu ý: sẽ chuyển câu truy vấn sang dạng distinct + group by để đếm logs.
     */
    public static Specification<ChatbotSessionEntity> minLogs(Integer minLogs) {
        return (root, query, cb) -> {
            // Đảm bảo dùng distinct để tránh nhân bản hàng
            query.distinct(true);
            // join tới logs
            var logsJoin = root.join("logs", JoinType.LEFT);
            // group by root.id để dùng hàm count
            query.groupBy(root.get("id"));
            // count(logs) >= minLogs
            return cb.greaterThanOrEqualTo(cb.count(logsJoin), minLogs.longValue());
        };
    }

    /**
     * Filter by maximum number of logs in the session.
     */
    public static Specification<ChatbotSessionEntity> maxLogs(Integer maxLogs) {
        return (root, query, cb) -> {
            query.distinct(true);
            var logsJoin = root.join("logs", JoinType.LEFT);
            query.groupBy(root.get("id"));
            return cb.lessThanOrEqualTo(cb.count(logsJoin), maxLogs.longValue());
        };
    }

    /**
     * Build a combined specification with optional filters.
     */
    public static Specification<ChatbotSessionEntity> build(
            String id,
            String userId,
            String title,
            Integer minLogs,
            Integer maxLogs
    ) {
        Specification<ChatbotSessionEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (userId != null && !userId.isBlank()) {
            spec = spec.and(hasUserId(userId));
        }
        if (title != null && !title.isBlank()) {
            spec = spec.and(titleLike(title));
        }
        if (minLogs != null) {
            spec = spec.and(minLogs(minLogs));
        }
        if (maxLogs != null) {
            spec = spec.and(maxLogs(maxLogs));
        }

        return spec;
    }
}
