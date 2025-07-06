// src/main/java/com/example/demo/reminder/controllers/dtos/CreateReminderRequest.java
package com.example.demo.reminder.controllers.dtos;

import com.example.demo.common.enums.FrequencyType;
import com.example.demo.common.enums.ReminderStatus;
import com.example.demo.common.enums.ScheduleType;
import com.example.demo.common.enums.WeekDay;
import jakarta.validation.constraints.*;
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
public class CreateReminderRequest {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 100, message = "Title at most 100 characters")
    String title;

    @NotBlank(message = "ActionType must not be blank")
    @Pattern(
            regexp = "^(WATERING|PRUNING|FERTILIZING|PEST_CHECK|HARVESTING|SEEDING|TRANSPLANTING|SOIL_CHECK|WEEDING|OTHER)$",
            message = "ActionType must be one of the enum values"
    )
    String actionType;

    @NotBlank(message = "ScheduleType must not be blank")
    @Pattern(
            regexp = "^(FIXED|RECURRING)$",
            message = "ScheduleType must be FIXED or RECURRING"
    )
    String scheduleType;

    // chỉ dùng khi FIXED
    LocalDateTime fixedDateTime;

    // chỉ dùng khi RECURRING
    @Pattern(
            regexp = "^(ONE_TIME|DAILY|WEEKLY|MONTHLY)$",
            message = "Frequency must be ONE_TIME, DAILY, WEEKLY or MONTHLY"
    )
    String frequency;

    // chỉ dùng khi RECURRING
    LocalTime timeOfDay;

    // chỉ dùng khi WEEKLY
    List<WeekDay> daysOfWeek;

    // chỉ dùng khi MONTHLY (1–31 hoặc -1)
    Integer dayOfMonth;

    @NotNull(message = "Status must not be null")
    ReminderStatus status;

    @NotBlank(message = "Garden ID must not be blank")
    String gardenId;


}
