package com.example.demo.community.like.controller.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Response DTO for LikeEntity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikeResponse {
    String id;
    String postId;
    String userId;
    LocalDateTime createdAt;
}
