package com.example.demo.user.controllers.dtos;

import com.example.demo.common.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String name;
    String email;
    String avatarLink;
    Role role;

    int gardenCount;
    int reminderCount;
    int postCount;
    int commentCount;
    int chatSessionCount;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isDeleted; // true nếu deletedAt != null
}
