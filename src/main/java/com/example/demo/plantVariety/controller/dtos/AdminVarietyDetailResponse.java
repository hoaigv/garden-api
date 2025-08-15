package com.example.demo.plantVariety.controller.dtos;

import com.example.demo.plantStage.controller.dtos.AdminPlantStageResponse;
import com.example.demo.plantStage.controller.dtos.PlantStageResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO for creating a new Plant Variety.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminVarietyDetailResponse {

    String id;

    String name;

    String description;

    String iconLink;

    String plantType;

    List<AdminPlantStageResponse> stages;
}
