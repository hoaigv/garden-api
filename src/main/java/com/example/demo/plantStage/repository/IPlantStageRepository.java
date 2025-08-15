package com.example.demo.plantStage.repository;

import com.example.demo.plantStage.model.PlantStageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for PlantStageEntity with CRUD and specification support.
 */
@Repository
public interface IPlantStageRepository
        extends JpaRepository<PlantStageEntity, String>, JpaSpecificationExecutor<PlantStageEntity> {
    // Additional query methods can be defined here
}
