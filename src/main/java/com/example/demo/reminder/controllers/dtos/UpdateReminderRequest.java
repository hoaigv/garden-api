// src/main/java/com/example/demo/reminder/controllers/dtos/UpdateReminderRequest.java
package com.example.demo.reminder.controllers.dtos;

import com.example.demo.common.enums.ReminderStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.example.demo.common.enums.WeekDay;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateReminderRequest {


    String id;

    @Size(max = 100, message = "Title at most 100 characters")
    String title;

    @Pattern(
            regexp = "^(WATERING|PRUNING|FERTILIZING|PEST_CHECK|HARVESTING|SEEDING|TRANSPLANTING|SOIL_CHECK|WEEDING|OTHER)$",
            message = "ActionType must be one of the enum values"
    )
    String actionType;

    @Pattern(
            regexp = "^(FIXED|RECURRING)$",
            message = "ScheduleType must be FIXED or RECURRING"
    )
    String scheduleType;

    LocalDateTime fixedDateTime;

    @Pattern(
            regexp = "^(ONE_TIME|DAILY|WEEKLY|MONTHLY)$",
            message = "Frequency must be ONE_TIME, DAILY, WEEKLY or MONTHLY"
    )
    String frequency;

    LocalTime timeOfDay;

    List<WeekDay> daysOfWeek;

    Integer dayOfMonth;

    ReminderStatus status;

    String gardenId;
}
