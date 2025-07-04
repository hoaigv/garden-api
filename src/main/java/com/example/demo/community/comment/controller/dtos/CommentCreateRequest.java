package com.example.demo.community.comment.controller.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Request DTO for creating a new comment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreateRequest {

    @NotNull(message = "Post ID must not be null")
    String postId;

    @NotBlank(message = "Content must not be blank")
    String content;
}
