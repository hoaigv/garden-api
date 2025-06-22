package com.example.demo.gardencell.controller.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchCreateGardenCellsRequest {
    @NotEmpty(message = "Cell list must not be empty")
    @Valid
    List<CreateGardenCellRequest> cells;
}
