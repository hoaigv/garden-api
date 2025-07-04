package com.example.demo.community.comment.controller.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.util.List;

/**
 * Request DTO for deleting one or more comments.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDeleteRequest {

    @NotEmpty(message = "List of comment IDs must not be empty")
    @NotNull(message = "List of comment IDs must not be null")
    List<String> ids;
}