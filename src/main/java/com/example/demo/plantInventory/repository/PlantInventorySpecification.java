
package com.example.demo.plantInventory.repository;

import com.example.demo.common.enums.PlantTypeEnum;
import com.example.demo.plantInventory.model.PlantInventoryEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for filtering PlantInventoryEntity queries.
 */
public class PlantInventorySpecification {

    /**
     * Filter by inventory ID (String).
     */
    public static Specification<PlantInventoryEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    /**
     * Filter by user ID.
     */
    public static Specification<PlantInventoryEntity> hasUserId(String userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    /**
     * Filter by plant type enum.
     */
    public static Specification<PlantInventoryEntity> hasPlantType(PlantTypeEnum type) {
        return (root, query, cb) -> cb.equal(root.get("plantType"), type);
    }

    /**
     * Filter by minimum inventory quantity.
     */
    public static Specification<PlantInventoryEntity> minQuantity(Integer minQty) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("inventoryQuantity"), minQty);
    }

    /**
     * Filter by maximum inventory quantity.
     */
    public static Specification<PlantInventoryEntity> maxQuantity(Integer maxQty) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("inventoryQuantity"), maxQty);
    }

    /**
     * Filter by minimum per-cell max.
     */
    public static Specification<PlantInventoryEntity> minPerCellMax(Integer minMax) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("perCellMax"), minMax);
    }

    /**
     * Filter by maximum per-cell max.
     */
    public static Specification<PlantInventoryEntity> maxPerCellMax(Integer maxMax) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("perCellMax"), maxMax);
    }

    /**
     * Build a combined specification with optional filters.
     */
    public static Specification<PlantInventoryEntity> build(
            String id,
            String userId,
            PlantTypeEnum type,
            Integer minQty,
            Integer maxQty,
            Integer minPerCellMax,
            Integer maxPerCellMax,
            String typePlant
    ) {
        Specification<PlantInventoryEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (userId != null && !userId.isBlank()) {
            spec = spec.and(hasUserId(userId));
        }
        if (type != null) {
            spec = spec.and(hasPlantType(type));
        }
        if (minQty != null) {
            spec = spec.and(minQuantity(minQty));
        }
        if (maxQty != null) {
            spec = spec.and(maxQuantity(maxQty));
        }
        if (minPerCellMax != null) {
            spec = spec.and(minPerCellMax(minPerCellMax));
        }
        if (maxPerCellMax != null) {
            spec = spec.and(maxPerCellMax(maxPerCellMax));
        }
        if (typePlant != null && !typePlant.isBlank()) {
            spec = spec.and(hasPlantType(PlantTypeEnum.valueOf(typePlant)));
        }

        return spec;
    }
}
