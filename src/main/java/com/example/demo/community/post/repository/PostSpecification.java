package com.example.demo.community.post.repository;

import com.example.demo.community.post.model.CommunityPostEntity;
import com.example.demo.user.model.UserEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PostSpecification {

    public static Specification<CommunityPostEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<CommunityPostEntity> hasUserId(String userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<CommunityPostEntity> titleLike(String title) {
        return (root, query, cb) -> cb.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<CommunityPostEntity> bodyLike(String body) {
        return (root, query, cb) -> cb.like(root.get("body"), "%" + body + "%");
    }

    public static Specification<CommunityPostEntity> createdAfter(LocalDateTime from) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<CommunityPostEntity> createdBefore(LocalDateTime to) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }

    public static Specification<CommunityPostEntity> isNotDelete() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    /**
     * Filter posts having comments (commentList not empty) or no comments
     */
    public static Specification<CommunityPostEntity> hasComments(boolean isComment) {
        return (root, query, cb) -> {
            Expression<Integer> size = cb.size(root.get("comments"));
            return isComment ? cb.greaterThan(size, 0) : cb.equal(size, 0);
        };
    }

    /**
     * Filter posts having likes (likeList not empty) or no likes
     */
    public static Specification<CommunityPostEntity> hasLikes(boolean isLike) {
        return (root, query, cb) -> {
            Expression<Integer> size = cb.size(root.get("likes"));
            return isLike ? cb.greaterThan(size, 0) : cb.equal(size, 0);
        };
    }

    /**
     * Build a dynamic specification with optional filters: userId, comment presence, like presence, created date range
     */
    public static Specification<CommunityPostEntity> buildCurrent(
            String userId,
            Boolean isComment,
            Boolean isLike

    ) {
        Specification<CommunityPostEntity> spec = (root, query, cb) -> cb.conjunction();


        if (userId != null && !userId.isBlank()) {
            spec = spec.and(hasUserId(userId));
        }
        if (isComment != null) {
            spec = spec.and(hasComments(isComment));
        }
        if (isLike != null) {
            spec = spec.and(hasLikes(isLike));
        }


        return spec;
    }

    /**
     * Original build method
     */
    public static Specification<CommunityPostEntity> build(
            String id,
            String userId,
            String body,
            boolean isNotDeleted ,
            LocalDateTime createdFrom,
            LocalDateTime createdTo
    ) {
        Specification<CommunityPostEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (body != null && !body.isBlank()) {
            spec = spec.and(bodyLike(body));
        }

        if(isNotDeleted){
            spec = spec.and(isNotDelete());
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
