
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

    public static Specification<PlantInventoryEntity> hasVarietyId(String varietyId) {
        return (root, query, cb) -> cb.equal(root.get("plantVariety").get("id"), varietyId);
    }


    /**
     * Build a combined specification with optional filters.
     */
    public static Specification<PlantInventoryEntity> build(
            String id,
            String userId

    ) {
        Specification<PlantInventoryEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (userId != null && !userId.isBlank()) {
            spec = spec.and(hasUserId(userId));
        }


        return spec;
    }
}
