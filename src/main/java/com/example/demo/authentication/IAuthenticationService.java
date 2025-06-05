package com.example.demo.authentication;


import com.example.demo.authentication.controllers.dtos.*;
import com.example.demo.common.ApiResponse;

public interface IAuthenticationService {
    ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest);
    ApiResponse<Void> logout();
    IntrospectResponse introspect(IntrospectRequest introspectRequest) ;
    ApiResponse<AuthenticationResponse>  refreshToken(RefreshRequest request) ;
}
