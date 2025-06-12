package com.example.demo.authentication.service;


import com.example.demo.authentication.controllers.dtos.*;
import com.example.demo.common.ApiResponse;

public interface IAuthenticationService<T> {
    ApiResponse<AuthenticationResponse> authenticate(T authenticationRequest);

    ApiResponse<Void> logout();

    IntrospectResponse introspect(IntrospectRequest introspectRequest);

    ApiResponse<AuthenticationResponse> refreshToken(RefreshRequest request);
}
