package com.example.demo.community.post.controller.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Response DTO for fetching a community post.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommunityPostResponse {

    String id;
    String body;
    String imageLink;
    Boolean isLike;
    Long totalComment;
    Long totalLike;
    String userName;
    String userLink;
    LocalDateTime createdAt;
}
