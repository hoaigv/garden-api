package com.example.demo.authentication.controllers.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserInfo {
    private String googleId;
    private String fullName;
    private String email;
    private String avatarUrl;
}