// src/main/java/com/example/demo/reminder/controller/dtos/CreateReminderRequest.java
package com.example.demo.reminder.controllers.dtos;

import com.example.demo.common.enums.ReminderStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReminderRequest {

    @NotBlank(message = "Task must not be blank")
    @Size(max = 100, message = "Task must be at most 100 characters")
    String task;

    @NotBlank(message = "GardenActivity must not be blank")
    @Pattern(
            regexp = "^(WATERING|FERTILIZING|PRUNING|PEST_CHECK|HARVESTING|SEEDING|TRANSPLANTING|SOIL_CHECK|WEEDING|OTHER)$",
            message = "GardenActivity must be one of: WATERING, FERTILIZING, PRUNING, PEST_CHECK, HARVESTING, SEEDING, TRANSPLANTING, SOIL_CHECK, WEEDING, OTHER"
    )
    String gardenActivity;


    // Thời gian cụ thể (nullable nếu lặp)
    LocalDateTime specificTime;

    @NotBlank(message = "Frequency must not be blank")
    @Pattern(
            regexp = "ONE_TIME|DAILY|WEEKLY|MONTHLY",
            message = "Frequency must be one of: ONE_TIME, DAILY, WEEKLY, MONTHLY"
    )
    String frequency;


    @NotNull(message = "Status must not be null")
    ReminderStatus status;

    @NotBlank(message = "Garden ID must not be blank")
    String gardenId;
}