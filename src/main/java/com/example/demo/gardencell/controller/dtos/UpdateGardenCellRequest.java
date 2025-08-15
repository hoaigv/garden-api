package com.example.demo.gardencell.controller.dtos;

import com.example.demo.common.enums.HealthStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateGardenCellRequest {
    @NotBlank(message = "Cell ID must not be blank")
    String id;


    @Min(value = 0, message = "Row index must be >= 0")
    Integer rowIndex;


    @Min(value = 0, message = "Column index must be >= 0")
    Integer colIndex;

    String healthStatus;

    @NotNull(message = "Next Stage must be Yes or No")
    Boolean nextStage;

    String diseaseName;


    String imgCellCurrent;

}
