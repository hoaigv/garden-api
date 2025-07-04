package com.example.demo.community.like.repository;

import com.example.demo.community.like.model.LikeEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class LikeSpecification {

    public static Specification<LikeEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<LikeEntity> hasPostId(String postId) {
        return (root, query, cb) -> cb.equal(root.get("post").get("id"), postId);
    }

    public static Specification<LikeEntity> hasUserId(String userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<LikeEntity> createdAfter(LocalDateTime from) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<LikeEntity> createdBefore(LocalDateTime to) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }

    public static Specification<LikeEntity> build(
            String id,
            String postId,
            String userId,
            LocalDateTime createdFrom,
            LocalDateTime createdTo
    ) {
        Specification<LikeEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (postId != null && !postId.isBlank()) {
            spec = spec.and(hasPostId(postId));
        }
        if (userId != null && !userId.isBlank()) {
            spec = spec.and(hasUserId(userId));
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
