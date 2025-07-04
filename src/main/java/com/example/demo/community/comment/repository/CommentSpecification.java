package com.example.demo.community.comment.repository;

import com.example.demo.community.comment.model.CommentEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CommentSpecification {

    public static Specification<CommentEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<CommentEntity> hasPostId(String postId) {
        return (root, query, cb) -> cb.equal(root.get("post").get("id"), postId);
    }

    public static Specification<CommentEntity> hasUserId(String userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<CommentEntity> contentLike(String content) {
        return (root, query, cb) -> cb.like(root.get("content"), "%" + content + "%");
    }

    public static Specification<CommentEntity> createdAfter(LocalDateTime from) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<CommentEntity> createdBefore(LocalDateTime to) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }

    public static Specification<CommentEntity> build(
            String id,
            String postId,
            String userId,
            String content,
            LocalDateTime createdFrom,
            LocalDateTime createdTo
    ) {
        Specification<CommentEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (postId != null && !postId.isBlank()) {
            spec = spec.and(hasPostId(postId));
        }
        if (userId != null && !userId.isBlank()) {
            spec = spec.and(hasUserId(userId));
        }
        if (content != null && !content.isBlank()) {
            spec = spec.and(contentLike(content));
        }
        if (createdFrom != null) {
            spec = spec.and(createdAfter(createdFrom));
        }
        if (createdTo != null) {
            spec = spec.and(createdBefore(createdTo));
        }

        return spec;
    }
}