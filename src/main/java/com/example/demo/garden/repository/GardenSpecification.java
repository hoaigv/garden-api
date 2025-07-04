package com.example.demo.garden.repository;

import com.example.demo.garden.model.GardenEntity;
import com.example.demo.common.enums.GardenCondition;
import com.example.demo.user.model.UserEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for filtering GardenEntity queries.
 */
public class GardenSpecification {

    /**
     * Filter by garden ID (String).
     */
    public static Specification<GardenEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public  static Specification<GardenEntity> isNotDelete() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }
    /**
     * Filter by user ID owning the garden.
     */
    public static Specification<GardenEntity> hasUserId(String userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    /**
     * Filter by garden name (like).
     */
    public static Specification<GardenEntity> nameLike(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    /**
     * Filter by garden condition.
     */
    public static Specification<GardenEntity> hasCondition(GardenCondition condition) {
        return (root, query, cb) -> cb.equal(root.get("gardenCondition"), condition);
    }

    /**
     * Filter by minimum number of rows (rowLength >=).
     */
    public static Specification<GardenEntity> minRows(Integer minRows) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("rowLength"), minRows);
    }

    /**
     * Filter by maximum number of rows (rowLength <=).
     */
    public static Specification<GardenEntity> maxRows(Integer maxRows) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("rowLength"), maxRows);
    }

    /**
     * Filter by minimum number of columns (colLength >=).
     */
    public static Specification<GardenEntity> minCols(Integer minCols) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("colLength"), minCols);
    }

    /**
     * Filter by maximum number of columns (colLength <=).
     */
    public static Specification<GardenEntity> maxCols(Integer maxCols) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("colLength"), maxCols);
    }

    /**
     * Build a combined specification with optional filters.
     */
    public static Specification<GardenEntity> build(
            String id,
            String userId,
            String name,
            String conditionStr,
            Integer minRows,
            Integer maxRows,
            Integer minCols,
            Integer maxCols
    ) {
        Specification<GardenEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (userId != null && !userId.isBlank()) {
            spec = spec.and(hasUserId(userId));
        }
        if (name != null && !name.isBlank()) {
            spec = spec.and(nameLike(name));
        }
        if (conditionStr != null && !conditionStr.isBlank()) {
            try {
                GardenCondition cond = GardenCondition.valueOf(conditionStr);
                spec = spec.and(hasCondition(cond));
            } catch (IllegalArgumentException e) {
                // skip invalid condition
            }
        }
        if (minRows != null) {
            spec = spec.and(minRows(minRows));
        }
        if (maxRows != null) {
            spec = spec.and(maxRows(maxRows));
        }
        if (minCols != null) {
            spec = spec.and(minCols(minCols));
        }
        if (maxCols != null) {
            spec = spec.and(maxCols(maxCols));
        }

        return spec;
    }
}
