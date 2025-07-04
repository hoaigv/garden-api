package com.example.demo.community.post.controller.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Request DTO for updating an existing community post.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommunityPostUpdateRequest {

    @NotNull(message = "Post ID must not be null")
    String id;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 150, message = "Title must be at most 150 characters")
    String title;

    @NotBlank(message = "Body must not be blank")
    String body;

    String imageLink;
}