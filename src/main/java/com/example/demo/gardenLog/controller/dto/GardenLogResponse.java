package com.example.demo.gardenLog.controller.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GardenLogResponse {
    private String id;
    private String description;
    private String actionType;
    private LocalDateTime timestamp;
    private String reminderId;
    private String gardenId;
    private String gardenCellId;
}
