package com.example.demo.reminder.repository;

import com.example.demo.reminder.model.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for ReminderEntity with support for Specifications.
 */
@Repository
public interface IReminderRepository extends JpaRepository<ReminderEntity, String>, JpaSpecificationExecutor<ReminderEntity> {
}