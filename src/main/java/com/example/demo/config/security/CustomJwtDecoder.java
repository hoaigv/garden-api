package com.example.demo.config.security;


import com.example.demo.authentication.controllers.dtos.AuthenticationRequest;
import com.example.demo.authentication.service.IAuthenticationService;
import com.example.demo.authentication.controllers.dtos.IntrospectRequest;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKeyAccess}")
    @NonFinal
    String SIGNER_KEY_ACCESS;

    private final IAuthenticationService<AuthenticationRequest> authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    public CustomJwtDecoder(IAuthenticationService<AuthenticationRequest> authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Jwt decode(String token) {
        var response =
                authenticationService.introspect(IntrospectRequest.builder().accessToken(token).build());
        if (!response.isValid()) throw new BadJwtException("Invalid token");
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY_ACCESS.getBytes(), "HS512");
            nimbusJwtDecoder =
                    NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
