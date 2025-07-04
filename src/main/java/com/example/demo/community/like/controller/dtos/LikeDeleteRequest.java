package com.example.demo.community.like.controller.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Request DTO for unliking (deleting) one or more likes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikeDeleteRequest {

    @NotEmpty(message = "List of like IDs must not be empty")
    @NotNull(message = "List of like IDs must not be null")
    List<String> ids;
}