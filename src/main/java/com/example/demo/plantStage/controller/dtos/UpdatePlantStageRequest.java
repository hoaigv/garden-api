package com.example.demo.plantStage.controller.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for creating a new Plant Stage.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePlantStageRequest {

    @NotNull(message = "Id of Stages can't be null ! ")
    @NotBlank(message = "Id of Stages can't be empty !")
    String id;

    @NotBlank(message = "Name of Variety can't be empty")
    String name;


    @NotBlank(message = "Icon link can't be Empty !")
    String iconLink;

    @NotBlank(message = "Description of Variety can't be empty !")
    String description;

    @Min(
            value = 0, message = "Estimated days must be greater than 0 !"
    )
    Integer estimatedByDay;

    @Min(
            value = 0, message = "Stage Order must be greater than 0 !"
    )
    Integer stageOrder;
}
