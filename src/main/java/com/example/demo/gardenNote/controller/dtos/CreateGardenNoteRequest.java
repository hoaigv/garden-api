// src/main/java/com/example/demo/gardenNote/controller/dtos/CreateGardenNoteRequest.java
package com.example.demo.gardenNote.controller.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateGardenNoteRequest {

    @NotBlank(message = "Note text must not be blank")
    @Size(max = 5000, message = "Note text must be at most 5000 characters")
    String noteText;

    @Size(max = 512, message = "Photo URL must be at most 512 characters")
    String photoUrl;

    @NotBlank(message = "Garden ID must not be blank")
    String gardenId;

    @NotBlank(message = " TiTle must not be blank")
    @NotNull(message = " TiTle must not be null")
    String noteTitle;

}
