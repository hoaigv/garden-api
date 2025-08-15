package com.example.demo.plantVariety.repository;

import com.example.demo.common.enums.PlantTypeEnum;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.plantVariety.model.PlantVarietyEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for filtering PlantVarietyEntity queries.
 */
public class PlantVarietySpecification {

    /**
     * Filter by variety ID (String).
     */
    public static Specification<PlantVarietyEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    /**
     * Filter by variety name (case-insensitive, partial match).
     */
    public static Specification<PlantVarietyEntity> hasName(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    /**
     * Filter by plant type enum.
     */
    public static Specification<PlantVarietyEntity> hasPlantType(PlantTypeEnum plantType) {
        return (root, query, cb) -> cb.equal(root.get("plantType"), plantType);
    }


    public static Specification<PlantVarietyEntity> isNotDelete() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }


    /**
     * Build a combined specification with optional filters.
     */
    public static Specification<PlantVarietyEntity> build(
            String id,
            String name,
            PlantTypeEnum plantType

            ) {
        Specification<PlantVarietyEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (name != null && !name.isBlank()) {
            spec = spec.and(hasName(name));
        }
        if (plantType != null) {
            spec = spec.and(hasPlantType(plantType));
        }


        return spec;
    }
}
