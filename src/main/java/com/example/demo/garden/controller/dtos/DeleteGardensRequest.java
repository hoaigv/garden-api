package com.example.demo.garden.controller.dtos;

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
public class DeleteGardensRequest {

    @NotEmpty(message = "List of garden IDs must not be empty")
    @NotNull(message = "List of garden IDs must not be null")
    List<String> ids;
}
