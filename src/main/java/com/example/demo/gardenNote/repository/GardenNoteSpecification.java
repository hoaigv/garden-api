// src/main/java/com/example/demo/gardenNote/repository/GardenNoteSpecification.java
package com.example.demo.gardenNote.repository;

import com.example.demo.gardenNote.model.GardenNoteEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public class GardenNoteSpecification {

    public static Specification<GardenNoteEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<GardenNoteEntity> hasGardenId(String gardenId) {
        return (root, query, cb) -> cb.equal(root.get("garden").get("id"), gardenId);
    }

    public static Specification<GardenNoteEntity> hasUserId(String userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<GardenNoteEntity> noteTextContains(String text) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("noteText")), "%" + text.toLowerCase() + "%");
    }

    public static Specification<GardenNoteEntity> createdAtBetween(OffsetDateTime from, OffsetDateTime to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("createdAt"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get("createdAt"), to);
            } else {
                return cb.conjunction();
            }
        };
    }

    /**
     * Build a combined specification with optional filters.
     */
    public static Specification<GardenNoteEntity> build(
            String id,
            String gardenId,
            String userId,
            String textContains,
            OffsetDateTime fromCreatedAt,
            OffsetDateTime toCreatedAt
    ) {
        Specification<GardenNoteEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (gardenId != null && !gardenId.isBlank()) {
            spec = spec.and(hasGardenId(gardenId));
        }
        if (userId != null && !userId.isBlank()) {
            spec = spec.and(hasUserId(userId));
        }
        if (textContains != null && !textContains.isBlank()) {
            spec = spec.and(noteTextContains(textContains));
        }
        if (fromCreatedAt != null || toCreatedAt != null) {
            spec = spec.and(createdAtBetween(fromCreatedAt, toCreatedAt));
        }

        return spec;
    }
}
