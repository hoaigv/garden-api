package com.example.demo.plantVariety.controller.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for creating a new Plant Variety.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePlantVarietyRequest {

    @NotBlank(message = "Name of Variety can't be empty")
    String name;

    @NotBlank(message = "Description of Variety can't be empty !")
    String description;

    @NotBlank(message = "Icon link can't be Empty !")
    String iconLink;

    @Pattern(
            regexp = "VEGETABLE|FRUIT|HERB|FLOWER|SHRUB|TREE",
            message = "Plant type must be one of: VEGETABLE, FRUIT, HERB, FLOWER, SHRUB, TREE"
    )
    String plantType;
}
