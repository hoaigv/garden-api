package com.example.demo.garden.controller.dtos;

import com.example.demo.common.enums.GardenCondition;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GardenResponse {
    String id;
    String name;
    Integer rowLength;
    Integer colLength;
    GardenCondition gardenCondition;
    String soil;
    String userId;
}
