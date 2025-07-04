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
public class CommunityGetCurrentRequest {
    Boolean isComment;
    Boolean isLike;
    LocalDateTime createdFrom;
    LocalDateTime createdTo;

}
