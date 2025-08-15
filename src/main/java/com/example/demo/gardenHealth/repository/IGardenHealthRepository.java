package com.example.demo.gardenHealth.repository;

import com.example.demo.gardenHealth.model.GardenHealthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IGardenHealthRepository extends JpaRepository<GardenHealthEntity, String> , JpaSpecificationExecutor<GardenHealthEntity> {
}
