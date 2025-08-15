package com.example.demo.plantVariety.repository;

import com.example.demo.plantVariety.model.PlantVarietyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for PlantInventoryEntity with CRUD and specification support.
 */
@Repository
public interface IPlantVarietyRepository
        extends JpaRepository<PlantVarietyEntity, String>, JpaSpecificationExecutor<PlantVarietyEntity> {
    // Additional query methods can be defined here
}
