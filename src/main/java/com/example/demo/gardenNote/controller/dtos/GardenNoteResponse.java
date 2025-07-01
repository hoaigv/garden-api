// src/main/java/com/example/demo/gardenNote/controller/dtos/GardenNoteResponse.java
package com.example.demo.gardenNote.controller.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GardenNoteResponse {

    String id;
    String noteText;
    String photoUrl;
    String noteTitle;
    String gardenId;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
