package com.example.demo.gardencell.controller.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteGardenCellsRequest {
    @NotEmpty(message = "List of cell IDs must not be empty")
    @NotNull(message = "List of cell IDs must not be null")
    List<String> ids;
}
