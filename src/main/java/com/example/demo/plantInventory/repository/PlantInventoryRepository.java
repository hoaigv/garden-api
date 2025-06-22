package com.example.demo.plantInventory.repository;

import com.example.demo.plantInventory.model.PlantInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for PlantInventoryEntity with CRUD and specification support.
 */
@Repository
public interface PlantInventoryRepository
        extends JpaRepository<PlantInventoryEntity, String>, JpaSpecificationExecutor<PlantInventoryEntity> {
    // Additional query methods can be defined here
}
