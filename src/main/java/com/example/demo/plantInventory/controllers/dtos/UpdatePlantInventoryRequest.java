package com.example.demo.plantInventory.controllers.dtos;

import com.example.demo.common.enums.PlantTypeEnum;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for updating an existing PlantInventory.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePlantInventoryRequest {
    @NotBlank(message = "Inventory ID must not be blank")
    String id;

    @Min(value = 1, message = "Number Of Varieties must be than 0 !")
    @Max(value = 9999, message = "Number of Varieties must be less than 9999 ")
    Integer numberOfVariety;
}

