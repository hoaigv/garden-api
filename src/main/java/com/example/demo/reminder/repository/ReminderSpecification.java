package com.example.demo.reminder.repository;

import com.example.demo.common.enums.FrequencyType;
import com.example.demo.common.enums.ReminderStatus;
import com.example.demo.common.enums.ScheduleType;
import com.example.demo.common.enums.WeekDay;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.reminder.model.ReminderEntity;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;


import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReminderSpecification {

    public static Specification<ReminderEntity> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<ReminderEntity> hasTitle(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<ReminderEntity> hasScheduleType(ScheduleType type) {
        return (root, query, cb) -> cb.equal(root.get("scheduleType"), type);
    }

    public static Specification<ReminderEntity> fixedDateTimeBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            Path<LocalDateTime> path = root.get("fixedDateTime");
            if (from != null && to != null) {
                return cb.between(path, from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(path, from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(path, to);
            }
            return cb.conjunction();
        };
    }

    public  static Specification<ReminderEntity> isNotDelete() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<ReminderEntity> hasFrequency(FrequencyType freq) {
        return (root, query, cb) -> cb.equal(root.get("frequency"), freq);
    }

    public static Specification<ReminderEntity> timeOfDayBetween(LocalTime from, LocalTime to) {
        return (root, query, cb) -> {
            Path<LocalTime> path = root.get("timeOfDay");
            if (from != null && to != null) {
                return cb.between(path, from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(path, from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(path, to);
            }
            return cb.conjunction();
        };
    }

    public static Specification<ReminderEntity> hasDayOfMonth(Integer day) {
        return (root, query, cb) -> cb.equal(root.get("dayOfMonth"), day);
    }

    public static Specification<ReminderEntity> hasWeekDay(WeekDay day) {
        return (root, query, cb) -> cb.isMember(day, root.get("daysOfWeek"));
    }

    public static Specification<ReminderEntity> hasStatus(ReminderStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<ReminderEntity> hasUserId(String userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<ReminderEntity> hasGardenId(String gardenId) {
        return (root, query, cb) -> cb.equal(root.get("garden").get("id"), gardenId);
    }

    /**
     * Build a combined specification with optional filters.
     */
    public static Specification<ReminderEntity> build(
            String id,
            String title,
            ScheduleType scheduleType,
            LocalDateTime fromFixed,
            LocalDateTime toFixed,
            FrequencyType frequency,
            LocalTime fromTime,
            LocalTime toTime,
            Integer dayOfMonth,
            WeekDay weekDay,
            ReminderStatus status,
            String userId,
            String gardenId
    ) {
        Specification<ReminderEntity> spec = (root, query, cb) -> cb.conjunction();

        if (id != null && !id.isBlank()) {
            spec = spec.and(hasId(id));
        }
        if (title != null && !title.isBlank()) {
            spec = spec.and(hasTitle(title));
        }
        if (scheduleType != null) {
            spec = spec.and(hasScheduleType(scheduleType));
        }
        if (fromFixed != null || toFixed != null) {
            spec = spec.and(fixedDateTimeBetween(fromFixed, toFixed));
        }
        if (frequency != null) {
            spec = spec.and(hasFrequency(frequency));
        }
        if (fromTime != null || toTime != null) {
            spec = spec.and(timeOfDayBetween(fromTime, toTime));
        }
        if (dayOfMonth != null) {
            spec = spec.and(hasDayOfMonth(dayOfMonth));
        }
        if (weekDay != null) {
            spec = spec.and(hasWeekDay(weekDay));
        }
        if (status != null) {
            spec = spec.and(hasStatus(status));
        }
        if (userId != null && !userId.isBlank()) {
            spec = spec.and(hasUserId(userId));
        }
        if (gardenId != null && !gardenId.isBlank()) {
            spec = spec.and(hasGardenId(gardenId));
        }

        return spec;
    }
}
