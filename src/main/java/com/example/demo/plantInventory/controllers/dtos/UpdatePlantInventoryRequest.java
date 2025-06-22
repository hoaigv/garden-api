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

    @NotNull(message = "Plant type must not be null")
    PlantTypeEnum plantType;

    @NotBlank(message = "Image URL must not be blank")
    @Size(max = 512, message = "Image URL must be at most 512 characters")
    String imageUrl;

    @NotNull(message = "Inventory quantity must not be null")
    @Min(value = 0, message = "Inventory quantity must be >= 0")
    Integer inventoryQuantity;

    @NotNull(message = "Per-cell max must not be null")
    @Min(value = 1, message = "Per-cell max must be >= 1")
    Integer perCellMax;
    @NotBlank(message = "Icon must not be blank")
    String icon;
    @NotBlank(message = "Description must not be blank")
    String description;
    @NotNull(message = "Plant name mus not be null")
    @NotBlank(message = "Plant name mus not be Empty")
    String name;
}

