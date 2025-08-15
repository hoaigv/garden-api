package com.example.demo.garden.controller.dtos;

import com.example.demo.common.enums.GardenCondition;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateGardenRequest {
    @NotNull(message = "Garden ID must not be null")
    String id;

    @NotBlank(message = "Garden name must not be blank")
    @NotNull(message = "Garden name must not be null")
    String name;

    @NotBlank(message = "Soil type must not be blank")
    @Pattern(
            regexp = "SANDY_SOIL|LOAMY_SAND|LOAM|CLAY_LOAM|CLAY_SOIL|ALLUVIAL_SOIL|PEATY_SOIL|CHALKY_SOIL|ACID_SULFATE_SOIL|BASALTIC_SOIL|RED_SOIL|BLACK_SOIL|INFERTILE_SOIL",
            message = "Soil type must be one of the predefined soil types"
    )
    String soil;
}
