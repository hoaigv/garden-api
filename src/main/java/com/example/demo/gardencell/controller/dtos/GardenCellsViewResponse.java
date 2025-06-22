package com.example.demo.gardencell.controller.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Wrapper DTO for returning a list of cell summaries for a given garden.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GardenCellsViewResponse {
    /**
     * The ID of the garden (as String).
     */
    String gardenId;

    int rowLength;

    int colLength;

    /**
     * List of individual cell summaries.
     */
    List<GardenCellResponse> cells;
}
