package com.example.demo.plantVariety.controller.dtos;

import com.example.demo.plantStage.controller.dtos.PlantStageResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class VarietyDetailResponse {

    String id;

    String name;

    String description;

    String iconLink;

    String plantType;

    List<PlantStageResponse> stages;
}
