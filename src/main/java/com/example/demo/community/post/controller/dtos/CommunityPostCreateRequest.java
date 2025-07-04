package com.example.demo.community.post.controller.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Request DTO for creating a new community post.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommunityPostCreateRequest {


    @NotBlank(message = "Body must not be blank")
    String body;

    String imageLink;

}