package com.example.demo.gardencell.controller.dtos;

import com.example.demo.common.enums.HealthStatus;
import com.example.demo.plantStage.controller.dtos.PlantStageResponse;
import com.example.demo.plantVariety.controller.dtos.PlantVarietyResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GardenCellResponse {
    String id;
    Integer rowIndex;
    Integer colIndex;
    Short quantity;
    HealthStatus healthStatus;
    String diseaseName;
    PlantVarietyResponse plantVariety;
    String stageLink;
    String stageGrow;
    LocalDateTime createdAt;
    String imgCellCurrent;
}
