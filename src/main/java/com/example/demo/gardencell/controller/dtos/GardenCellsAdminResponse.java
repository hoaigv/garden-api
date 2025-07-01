package com.example.demo.gardencell.controller.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GardenCellsAdminResponse {
    GardenCellsViewResponse gardenCellsViewResponse;
    String userName;
    String gardenName;
    Set<String> inventoryPlantName;
    String gardenCondition;
    LocalDateTime createAt;
}
