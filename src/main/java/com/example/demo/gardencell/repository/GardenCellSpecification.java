package com.example.demo.gardencell.repository;

import com.example.demo.gardencell.model.GardenCellEntity;
import com.example.demo.common.enums.HealthStatus;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for filtering GardenCellEntity queries.
 */
public class GardenCellSpecification {

    /**
     * Filter by garden ID (String).
     */
    public static Specification<GardenCellEntity> hasGardenId(String gardenId) {
        return (root, query, cb) -> cb.equal(root.get("garden").get("id"), gardenId);
    }

    /**
     * Filter by plant inventory ID (String).
     */
    public static Specification<GardenCellEntity> hasPlantInventoryId(String plantInventoryId) {
        return (root, query, cb) -> cb.equal(root.get("plantInventory").get("id"), plantInventoryId);
    }

    /**
     * Filter by health status.
     */
    public static Specification<GardenCellEntity> hasHealthStatus(HealthStatus status) {
        return (root, query, cb) -> cb.equal(root.get("healthStatus"), status);
    }

    /**
     * Filter by minimum quantity.
     */
    public static Specification<GardenCellEntity> quantityGreaterThanOrEqual(Short minQuantity) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("quantity"), minQuantity);
    }

    /**
     * Filter by maximum quantity.
     */
    public static Specification<GardenCellEntity> quantityLessThanOrEqual(Short maxQuantity) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("quantity"), maxQuantity);
    }

    /**
     * Filter by row index.
     */
    public static Specification<GardenCellEntity> hasRowIndex(Integer rowIndex) {
        return (root, query, cb) -> cb.equal(root.get("rowIndex"), rowIndex);
    }

    /**
     * Filter by column index.
     */
    public static Specification<GardenCellEntity> hasColIndex(Integer colIndex) {
        return (root, query, cb) -> cb.equal(root.get("colIndex"), colIndex);
    }

    /**
     * Build combined specification. Null checks skip that filter.
     * Uses a conjunction (always-true) base instead of deprecated Specification.where(null).
     */
    public static Specification<GardenCellEntity> build(
            String gardenId,
            String plantInventoryId,
            HealthStatus status
    ) {
        // Start with a no-op conjunction
        Specification<GardenCellEntity> spec = (root, query, cb) -> cb.conjunction();

        if (gardenId != null && !gardenId.isBlank()) {
            spec = spec.and(hasGardenId(gardenId));
        }
        if (plantInventoryId != null && !plantInventoryId.isBlank()) {
            spec = spec.and(hasPlantInventoryId(plantInventoryId));
        }
        if (status != null) {
            spec = spec.and(hasHealthStatus(status));
        }

        return spec;
    }
}
