package com.example.demo.plantInventory.controllers.dtos;

import com.example.demo.common.enums.PlantTypeEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;


/**
 * DTO returned to client for PlantInventory data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantInventoryResponse {
    String id;
    String userId;
    PlantTypeEnum plantType;
    String imageUrl;
    Integer inventoryQuantity;
    Integer perCellMax;
    String description;
    String name;
    String icon;
}
