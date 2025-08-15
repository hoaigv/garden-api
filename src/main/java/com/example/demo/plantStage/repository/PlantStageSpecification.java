package com.example.demo.plantStage.repository;

import com.example.demo.plantStage.model.PlantStageEntity;
import com.example.demo.plantVariety.model.PlantVarietyEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for filtering PlantStageEntity queries.
 */
public class PlantStageSpecification {

    /**
     * Filter by stage ID (String).
     */
    public static Specification<PlantStageEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    /**
     * Filter by stage name (case-insensitive, partial match).
     */
    public static Specification<PlantStageEntity> hasName(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    /**
     * Filter by exact stageOrder.
     */
    public static Specification<PlantStageEntity> hasStageOrder(Integer order) {
        return (root, query, cb) -> cb.equal(root.get("stageOrder"), order);
    }

    /**
     * Filter by minimum estimatedByDay (>=).
     */
    public static Specification<PlantStageEntity> estimatedByDayGreaterOrEqual(Integer days) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("estimatedByDay"), days);
    }

    public static Specification<PlantStageEntity> isNotDelete() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    /**
     * Filter by PlantVariety ID.
     */
    public static Specification<PlantStageEntity> hasPlantVarietyId(String varietyId) {
        return (root, query, cb) -> cb.equal(root.get("plantVariety").get("id"), varietyId);
    }

    /**
     * Build a combined specification with optional filters.
     */
    public static Specification<PlantStageEntity> build(
            String id,
            String name,
            Integer stageOrder,
            Integer minEstimatedDays,
            String plantVarietyId
    ) {
        Specification<PlantStageEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (name != null && !name.isBlank()) {
            spec = spec.and(hasName(name));
        }
        if (stageOrder != null) {
            spec = spec.and(hasStageOrder(stageOrder));
        }
        if (minEstimatedDays != null) {
            spec = spec.and(estimatedByDayGreaterOrEqual(minEstimatedDays));
        }
        if (plantVarietyId != null && !plantVarietyId.isBlank()) {
            spec = spec.and(hasPlantVarietyId(plantVarietyId));
        }

        return spec;
    }
}
