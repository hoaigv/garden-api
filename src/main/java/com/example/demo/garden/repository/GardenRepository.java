package com.example.demo.garden.repository;


import com.example.demo.garden.model.GardenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GardenRepository
        extends JpaRepository<GardenEntity, String>, JpaSpecificationExecutor<GardenEntity> {


}
