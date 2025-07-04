package com.example.demo.community.like.controller.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Request DTO for creating a new like on a post.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikeCreateRequest {

    @NotNull(message = "Post ID must not be null")
    String postId;

    // userId will be taken from security context in service layer
}