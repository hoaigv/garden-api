package com.example.demo.notification.repository;

import com.example.demo.notification.model.NotificationEntity;

import com.example.demo.reminder.model.ReminderEntity;
import com.example.demo.user.model.UserEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;


import java.time.LocalDateTime;

/**
 * Specifications for filtering NotificationEntity queries.
 */
public class NotificationSpecification {

    /**
     * Filter by notification ID.
     */
    public static Specification<NotificationEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    /**
     * Filter by user ID owning the notification.
     */
    public static Specification<NotificationEntity> hasUserId(String userId) {
        return (root, query, cb) -> {
            Join<NotificationEntity, UserEntity> userJoin = root.join("user");
            return cb.equal(userJoin.get("id"), userId);
        };
    }

    /**
     * Filter by title containing keyword.
     */
    public static Specification<NotificationEntity> titleLike(String keyword) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
    }


    public static Specification<NotificationEntity> hasReminderId(String reminderId) {
        return (root, query, cb) -> {
            Join<NotificationEntity, ReminderEntity> reminderJoin = root.join("reminder");
            return cb.equal(reminderJoin.get("id"), reminderId);
        };
    }

    /**
     * Filter by creation date range.
     */
    public static Specification<NotificationEntity> createdBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> cb.between(root.get("createdAt"), from, to);
    }

    /**
     * Filter by update date range.
     */
    public static Specification<NotificationEntity> updatedBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> cb.between(root.get("updatedAt"), from, to);
    }

    /**
     * Build combined specification with optional filters.
     */
    public static Specification<NotificationEntity> build(
            String id,
            String userId,
            String title,
            String reminderId,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            LocalDateTime updatedFrom,
            LocalDateTime updatedTo
    ) {
        Specification<NotificationEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (userId != null && !userId.isBlank()) {
            spec = spec.and(hasUserId(userId));
        }
        if (title != null && !title.isBlank()) {
            spec = spec.and(titleLike(title));
        }
        if (reminderId != null && !reminderId.isBlank()) {
            spec = spec.and(hasReminderId(reminderId));
        }

        if (createdFrom != null && createdTo != null) {
            spec = spec.and(createdBetween(createdFrom, createdTo));
        }
        if (updatedFrom != null && updatedTo != null) {
            spec = spec.and(updatedBetween(updatedFrom, updatedTo));
        }

        return spec;
    }
}