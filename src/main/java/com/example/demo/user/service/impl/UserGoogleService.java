package com.example.demo.user.service.impl;

import com.example.demo.authentication.controllers.dtos.GoogleUserInfo;
import com.example.demo.common.ApiResponse;
import com.example.demo.common.enums.Role;
import com.example.demo.user.model.UserEntity;
import com.example.demo.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("userGoogleService")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserGoogleService extends BaseUserService<GoogleUserInfo> {
    UserRepository userRepository;


    @Override
    public ApiResponse<Void> createUser(GoogleUserInfo registerRequest) {
        var user = UserEntity.builder()
                .googleId(registerRequest.getGoogleId())
                .role(Role.USER)
                .name(registerRequest.getFullName())
                .avatarLink(registerRequest.getAvatarUrl())
                .email(registerRequest.getEmail())
                .build();

        userRepository.save(user);

        return ApiResponse.<Void>builder()
                .message("Create account successfully")
                .build();
    }
}
