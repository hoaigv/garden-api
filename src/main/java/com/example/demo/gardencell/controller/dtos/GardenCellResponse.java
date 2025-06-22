package com.example.demo.gardencell.controller.dtos;

import com.example.demo.common.enums.HealthStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GardenCellResponse {
    String id;
    Integer rowIndex;
    Integer colIndex;
    Short quantity;
    HealthStatus healthStatus;

    String plantInventoryId;   // ID of the plant type
    String icon;      // URL or base64 of the plant image
}
