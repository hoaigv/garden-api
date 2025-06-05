package com.example.demo.authentication.controllers.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {

    String id;

    String accessToken;

    String refreshToken;

    String role;

    String name;

    String avatar_link;
}
