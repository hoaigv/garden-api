package com.example.demo.reminder.controllers.dtos;

import com.example.demo.common.enums.ReminderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReminderResponse {

    String id;
    String task;
    String gardenActivity;
    LocalDateTime specificTime;
    String frequency;
    ReminderStatus status;
    String gardenId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}