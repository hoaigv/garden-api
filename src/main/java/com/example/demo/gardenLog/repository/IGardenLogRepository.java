package com.example.demo.gardenLog.repository;

import com.example.demo.gardenLog.model.GardenLogEntity;
import com.example.demo.reminder.model.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ReminderEntity with support for Specifications.
 */
@Repository
public interface IGardenLogRepository extends JpaRepository<GardenLogEntity, String> {

    Optional<List<GardenLogEntity>> findByGardenId(String id);
}