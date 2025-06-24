package com.example.demo.reminder.repository;

import com.example.demo.common.enums.ReminderStatus;
import com.example.demo.reminder.model.ReminderEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Specifications for filtering ReminderEntity queries.
 */
public class ReminderSpecification {

    public static Specification<ReminderEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<ReminderEntity> hasGardenId(String gardenId) {
        return (root, query, cb) -> cb.equal(root.get("garden").get("id"), gardenId);
    }

    public static Specification<ReminderEntity> hasStatus(ReminderStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<ReminderEntity> hasFrequency(String frequency) {
        return (root, query, cb) -> cb.equal(root.get("frequency"), frequency);
    }

    public static Specification<ReminderEntity> specificTimeBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("specificTime"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("specificTime"), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get("specificTime"), to);
            } else {
                return cb.conjunction();
            }
        };
    }

    /**
     * Build a combined specification with optional filters.
     */
    public static Specification<ReminderEntity> build(
            String id,
            String gardenId,
            String status,
            String frequency,
            LocalDateTime fromTime,
            LocalDateTime toTime
    ) {
        Specification<ReminderEntity> spec =(root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (gardenId != null && !gardenId.isBlank()) {
            spec = spec.and(hasGardenId(gardenId));
        }
        if (status != null && !status.isBlank()) {
            spec = spec.and(hasStatus(ReminderStatus.valueOf(status)));
        }
        if (frequency != null && !frequency.isBlank()) {
            spec = spec.and(hasFrequency(frequency));
        }
        if (fromTime != null || toTime != null) {
            spec = spec.and(specificTimeBetween(fromTime, toTime));
        }

        return spec;
    }
}
