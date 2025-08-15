package com.example.demo.plantStage.controller.dtos;

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
public class DeleteStagesRequest {

    @NotEmpty(message = "List of stage IDs must not be empty")
    @NotNull(message = "List of stage IDs must not be null")
    List<String> ids;

    @NotNull(message = "Variety Id mustn't be Null !")
    @NotBlank(message = "Variety Id mustn't be empty !")
    String varietyId;
}
