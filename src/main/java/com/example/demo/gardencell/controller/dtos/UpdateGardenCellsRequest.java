package com.example.demo.gardencell.controller.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateGardenCellsRequest {

    @NotEmpty(message = "cells list must not be empty")
    @Valid
    List<UpdateGardenCellRequest> cells;

}
