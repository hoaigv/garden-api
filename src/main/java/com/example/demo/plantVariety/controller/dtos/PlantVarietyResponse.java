package com.example.demo.plantVariety.controller.dtos;

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
public class PlantVarietyResponse {

    String id;

    String name;

    String iconLink;

    String plantType;
}
