package com.example.demo.gardencell.repository;

import com.example.demo.gardencell.model.GardenCellEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for GardenCellEntity with CRUD and Specifications support.
 */
@Repository
public interface IGardenCellRepository
        extends JpaRepository<GardenCellEntity, String>, JpaSpecificationExecutor<GardenCellEntity> {
    // You can define custom JPQL or query methods here if needed
}


