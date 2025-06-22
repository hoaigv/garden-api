package com.example.demo.plantInventory.controllers.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


/**
 * DTO for deleting one or more PlantInventory entries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeletePlantInventoriesRequest {
    @NotEmpty(message = "List of inventory IDs must not be empty")
    @NotNull(message = "List of inventory IDs must not be null")
    List<String> ids;
}
