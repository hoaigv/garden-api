// src/main/java/com/example/demo/reminder/controllers/dtos/ReminderResponse.java
package com.example.demo.reminder.controllers.dtos;

import com.example.demo.common.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReminderResponse {

    String id;
    String title;
    ActionType actionType;
    ScheduleType scheduleType;
    LocalDateTime fixedDateTime;
    FrequencyType frequency;
    LocalTime timeOfDay;
    List<WeekDay> daysOfWeek;
    Integer dayOfMonth;
    ReminderStatus status;
    String gardenId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
    String gardenName;
}
