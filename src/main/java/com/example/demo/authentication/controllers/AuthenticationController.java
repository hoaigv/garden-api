package com.example.demo.authentication.controllers;


import com.example.demo.authentication.controllers.dtos.*;
import com.example.demo.authentication.service.IAuthenticationService;
import com.example.demo.common.ApiResponse;
import com.example.demo.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    IAuthenticationService<GoogleAuthRequest> googleAuthService;


    IAuthenticationService<AuthenticationRequest> emailAuthService;

    IUserService<RegisterRequest> userService;


    @PostMapping("/google")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticateWithGoogle(@RequestBody @Valid GoogleAuthRequest googleAuthRequest) {


        return ResponseEntity.ok(googleAuthService.authenticate(googleAuthRequest));
    }

    @PostMapping("/email")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticateWithEmail(@RequestBody @Valid AuthenticationRequest authenticationRequest) {

        return ResponseEntity.ok(emailAuthService.authenticate(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid RegisterRequest registerRequest) {

        return ResponseEntity.ok(userService.createUser(registerRequest));
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {

        return ResponseEntity.ok(emailAuthService.logout());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(@RequestBody @Valid RefreshRequest request) {

        return ResponseEntity.ok(emailAuthService.refreshToken(request));
    }


}
