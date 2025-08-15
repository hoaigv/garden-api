package com.example.demo.plantStage.controller.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantStageResponse {
    String id;

    String name;

    String iconLink;

    String description;

    Integer stageOrder;

    Integer estimatedByDay;
}
