package com.example.demo.community.comment.controller.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Response DTO for fetching a comment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String postId;
    String userName;
    String userLink;
    String content;
    LocalDateTime createdAt;
}