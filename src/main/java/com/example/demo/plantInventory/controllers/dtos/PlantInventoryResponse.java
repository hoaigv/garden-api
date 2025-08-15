package com.example.demo.plantInventory.controllers.dtos;

import com.example.demo.common.enums.PlantTypeEnum;
import com.example.demo.plantVariety.controller.dtos.PlantVarietyResponse;
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
    Integer numberOfVariety;
    PlantVarietyResponse plantVariety;
}
