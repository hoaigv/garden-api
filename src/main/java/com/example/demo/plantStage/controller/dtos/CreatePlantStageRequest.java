package com.example.demo.plantStage.controller.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class CreatePlantStageRequest {

    @NotBlank
    @NotNull
    String varietyId;

    @NotNull(message = "Name of Stages can't be Null")
    @NotBlank(message = "Name of Stages can't be empty")
    String name;

    @NotNull(message = "Icon link can't be Null !")
    @NotBlank(message = "Icon link can't be Empty !")
    String iconLink;

    @NotBlank(message = "Description of Variety can't be empty !")
    String description;

    @Min(
            value = 0, message = "Estimated days must be greater than 0 !"
    )
    @NotNull(message = "Estimated days must not be null !")
    Integer estimatedByDay;

    @Min(
            value = 0, message = "Stage Order must be greater than 0 !"
    )
    @NotNull(message = "Stage Order must not be null !")
    Integer stageOrder;

}
