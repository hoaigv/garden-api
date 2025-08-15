package com.example.demo.gardencell.controller.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "garden Id mustn't be null !")
    @NotBlank(message = "garden Id mustn't be  empty !")
    String gardenId;
}
