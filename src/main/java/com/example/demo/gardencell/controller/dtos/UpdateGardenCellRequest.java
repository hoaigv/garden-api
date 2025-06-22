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

    @NotNull(message = "Row index must not be null")
    @Min(value = 0, message = "Row index must be >= 0")
    Integer rowIndex;

    @NotNull(message = "Column index must not be null")
    @Min(value = 0, message = "Column index must be >= 0")
    Integer colIndex;

    @NotNull(message = "Health status must not be null")
    HealthStatus healthStatus;
}
