package com.example.demo.garden.controller.dtos;

import com.example.demo.common.enums.GardenCondition;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateGardenRequest {
    @NotNull(message = "Garden ID must not be null")
    String id;

    @NotBlank(message = "Garden name must not be blank")
    @NotNull(message = "Garden name must not be null")
    String name;
}
