package com.example.demo.authentication.service.impl;

import com.example.demo.authentication.controllers.dtos.AuthenticationResponse;
import com.example.demo.authentication.controllers.dtos.GoogleAuthRequest;
import com.example.demo.authentication.controllers.dtos.GoogleUserInfo;
import com.example.demo.common.ApiResponse;
import com.example.demo.config.security.GoogleTokenVerifier;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.user.model.UserEntity;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.UserSpecification;
import com.example.demo.user.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("googleAuthService")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleAuthenticationService extends AuthBaseService<GoogleAuthRequest> {
    GoogleTokenVerifier googleTokenVerifier;
    IUserService<GoogleUserInfo> userService;
    UserRepository userRepository;

    @Override
    public ApiResponse<AuthenticationResponse> authenticate(GoogleAuthRequest auth) {

        var userInfo = googleTokenVerifier.verify(auth.getTokenId());
        UserEntity user;
        if (userRepository.findOne(UserSpecification.hasGoogleId(auth.getTokenId()).and(UserSpecification.isNotDelete())).isPresent()) {
            user = userRepository.findOne(UserSpecification.isNotDelete().and(UserSpecification.hasGoogleId(userInfo.getGoogleId()))).orElseThrow(
                    () -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND)
            );
        } else if (userRepository.findOne(UserSpecification.isNotDelete().and(UserSpecification.hasEmail(userInfo.getEmail()))).isPresent()) {
            user = userRepository.findOne(UserSpecification.isNotDelete().and(UserSpecification.hasEmail(userInfo.getEmail())))
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
            user.setGoogleId(userInfo.getGoogleId());
            userRepository.save(user);
        } else {
            userService.createUser(userInfo);
            user = userRepository.findOne(UserSpecification.hasGoogleId(userInfo.getGoogleId()).and(UserSpecification.isNotDelete()))
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        }

        return generateAuthenticationResponse(user);
    }
}
