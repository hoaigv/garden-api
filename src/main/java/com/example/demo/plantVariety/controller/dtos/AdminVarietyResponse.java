package com.example.demo.plantVariety.controller.dtos;

import com.example.demo.plantStage.controller.dtos.PlantStageResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminVarietyResponse {
    String id;

    String name;

    String iconLink;

    String plantType;

    Integer numCellsWithVariety;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    LocalDateTime deletedAt;
}
