package com.example.demo.garden.controller.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateGardenRequest {
    @NotBlank(message = "Garden name must not be blank")
    @NotNull(message = "Garden name must not be null")
    String name;

    @NotNull(message = "Row length must not be null")
    @Min(value = 1, message = "Row length must be at least 1")
    @Max(value = 6, message = "Row length max = 6")
    Integer rowLength;

    @NotNull(message = "Column length must not be null")
    @Min(value = 1, message = "Column length must be at least 1")
    @Max(value = 6, message = "Column length max = 6")
    Integer colLength;

    @NotBlank(message = "Soil type must not be blank")
    @NotNull(message = "Soil type must not be null")
    @Pattern(
            regexp = "SANDY_SOIL|LOAMY_SAND|LOAM|CLAY_LOAM|CLAY_SOIL|ALLUVIAL_SOIL|PEATY_SOIL|CHALKY_SOIL|ACID_SULFATE_SOIL|BASALTIC_SOIL|RED_SOIL|BLACK_SOIL|INFERTILE_SOIL",
            message = "Soil type must be one of the predefined soil types"
    )
    String soil;
}
