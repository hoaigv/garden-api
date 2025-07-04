package com.example.demo.community.post.controller.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Request DTO for deleting one or more community posts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommunityPostDeleteRequest {

    @NotEmpty(message = "List of post IDs must not be empty")
    @NotNull(message = "List of post IDs must not be null")
    List<String> ids;
}