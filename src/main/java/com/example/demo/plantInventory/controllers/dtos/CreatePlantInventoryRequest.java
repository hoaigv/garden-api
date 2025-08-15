package com.example.demo.plantInventory.controllers.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for creating a new PlantInventory.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePlantInventoryRequest {

    @NotNull(message = "Plant Variety Id can't be null !")
    @NotBlank(message = "Plant Variety Id can't be empty !")
    String varietyId;

    @Min(value = 1, message = "Number Of Varieties must be than 0 !")
    @Max(value = 9999, message = "Number of Varieties must be less than 9999 ")
    Integer numberOfVariety;

}
