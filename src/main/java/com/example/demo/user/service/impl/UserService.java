package com.example.demo.user.service.impl;

import com.example.demo.authentication.controllers.dtos.RegisterRequest;
import com.example.demo.common.ApiResponse;
import com.example.demo.common.enums.Role;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.user.model.UserEntity;
import com.example.demo.user.repository.IUserRepository;
import com.example.demo.user.repository.UserSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService extends BaseUserService<RegisterRequest> {

    IUserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public ApiResponse<Void> createUser(RegisterRequest registerRequest) {
        if (userRepository.findOne(UserSpecification.hasEmail(registerRequest.getEmail())).isPresent()) {
            throw new CustomRuntimeException(ErrorCode.USER_ALREADY_EXISTS);
        }
        var userEntity = UserEntity.builder()
                .email(registerRequest.getEmail())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .name("user_" + UUID.randomUUID().toString().substring(0, 6))
                .build();
        userRepository.save(userEntity);

        return ApiResponse.<Void>builder()
                .message("Register successfully !")
                .build();
    }


}
