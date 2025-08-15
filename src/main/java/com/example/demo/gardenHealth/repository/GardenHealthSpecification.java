package com.example.demo.gardenHealth.repository;

import com.example.demo.gardenHealth.model.GardenHealthEntity;
import com.example.demo.common.enums.HealthStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Specifications for filtering GardenHealthEntity queries.
 */
public class GardenHealthSpecification {

    public static Specification<GardenHealthEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<GardenHealthEntity> isNotDelete() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    /**
     * Filter by garden id (the referenced GardenEntity's id).
     */
    public static Specification<GardenHealthEntity> hasGardenId(String gardenId) {
        return (root, query, cb) -> cb.equal(root.get("garden").get("id"), gardenId);
    }

    /**
     * Filter by the owner user id of the garden (useful when joining through garden -> user).
     */
    public static Specification<GardenHealthEntity> hasGardenUserId(String userId) {
        return (root, query, cb) -> cb.equal(root.get("garden").get("user").get("id"), userId);
    }

    /**
     * diseaseName LIKE %name%
     */
    public static Specification<GardenHealthEntity> diseaseNameLike(String name) {
        return (root, query, cb) -> cb.like(root.get("diseaseName"), "%" + name + "%");
    }

    /**
     * Filter by health status enum
     */
    public static Specification<GardenHealthEntity> hasHealthStatus(HealthStatus status) {
        return (root, query, cb) -> cb.equal(root.get("healthStatus"), status);
    }

    /* Numeric cell filters */
    public static Specification<GardenHealthEntity> minNormalCell(Integer value) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("normalCell"), value);
    }

    public static Specification<GardenHealthEntity> maxNormalCell(Integer value) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("normalCell"), value);
    }

    public static Specification<GardenHealthEntity> minDeadCell(Integer value) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("deadCell"), value);
    }

    public static Specification<GardenHealthEntity> maxDeadCell(Integer value) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("deadCell"), value);
    }

    public static Specification<GardenHealthEntity> minDiseaseCell(Integer value) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("diseaseCell"), value);
    }

    public static Specification<GardenHealthEntity> maxDiseaseCell(Integer value) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("diseaseCell"), value);
    }

    /**
     * Filter by createdAt between from and to (inclusive). If one side is null, only apply the other.
     */
    public static Specification<GardenHealthEntity> createdAfter(LocalDateTime from) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<GardenHealthEntity> createdBefore(LocalDateTime to) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }

    /**
     * Build combined specification from optional parameters.
     */
    public static Specification<GardenHealthEntity> build(
            String id,
            String gardenId,
            String gardenUserId,
            String diseaseName,
            String healthStatusStr,
            Integer minNormal,
            Integer maxNormal,
            Integer minDead,
            Integer maxDead,
            Integer minDiseaseCell,
            Integer maxDiseaseCell,
            LocalDateTime createdFrom,
            LocalDateTime createdTo
    ) {
        Specification<GardenHealthEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (gardenId != null && !gardenId.isBlank()) {
            spec = spec.and(hasGardenId(gardenId));
        }
        if (gardenUserId != null && !gardenUserId.isBlank()) {
            spec = spec.and(hasGardenUserId(gardenUserId));
        }
        if (diseaseName != null && !diseaseName.isBlank()) {
            spec = spec.and(diseaseNameLike(diseaseName));
        }
        if (healthStatusStr != null && !healthStatusStr.isBlank()) {
            try {
                HealthStatus hs = HealthStatus.valueOf(healthStatusStr);
                spec = spec.and(hasHealthStatus(hs));
            } catch (IllegalArgumentException e) {
                // invalid enum value -> skip filter
            }
        }
        if (minNormal != null) {
            spec = spec.and(minNormalCell(minNormal));
        }
        if (maxNormal != null) {
            spec = spec.and(maxNormalCell(maxNormal));
        }
        if (minDead != null) {
            spec = spec.and(minDeadCell(minDead));
        }
        if (maxDead != null) {
            spec = spec.and(maxDeadCell(maxDead));
        }
        if (minDiseaseCell != null) {
            spec = spec.and(minDiseaseCell(minDiseaseCell));
        }
        if (maxDiseaseCell != null) {
            spec = spec.and(maxDiseaseCell(maxDiseaseCell));
        }
        if (createdFrom != null) {
            spec = spec.and(createdAfter(createdFrom));
        }
        if (createdTo != null) {
            spec = spec.and(createdBefore(createdTo));
        }

        // Always exclude soft-deleted by default
        spec = spec.and(isNotDelete());

        return spec;
    }
}
