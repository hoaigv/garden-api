// src/main/java/com/example/demo/gardenNote/controller/dtos/DeleteGardenNoteRequest.java
package com.example.demo.gardenNote.controller.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteGardenNoteRequest {

    /**
     * Danh sách ID của các ghi chú cần xoá.
     * Ít nhất phải có 1 ID.
     */
    @NotEmpty(message = "At least one GardenNote ID must be provided")
    List<@NotBlank(message = "GardenNote ID must not be blank") String> ids;
}
