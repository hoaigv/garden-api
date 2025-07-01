package com.example.demo.authentication.service.impl;


import com.example.demo.authentication.controllers.dtos.*;
import com.example.demo.common.ApiResponse;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.user.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("emailAuthService")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BasicAuthenticationService extends AuthBaseService<AuthenticationRequest> {

    IUserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public ApiResponse<AuthenticationResponse> authenticate(
            AuthenticationRequest authenticationRequest) {
        var user =
                userRepository
                        .findByEmailAndNotDeleted(authenticationRequest.getEmail())
                        .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));


        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPasswordHash())) {
            throw new CustomRuntimeException(ErrorCode.PASSWORD_INCORRECT);
        }
        return generateAuthenticationResponse(user);

    }


}
