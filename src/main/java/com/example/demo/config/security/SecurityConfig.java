package com.example.demo.config.security;


import com.example.demo.common.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SecurityConfig {
    CustomJwtDecoder customJwtDecoder;
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    RestAccessDeniedHandler restAccessDeniedHandler;
    static final String[] PUBLIC_POST_ENDPOINT = {
            "/api/auth/register", "/api/auth/**", "/api/auth/refresh"
    };

    static final String[] PRIVATE_ADMIN_GET_ENDPOINT = {
            "/api/users/**"
    };

    static final String[] PRIVATE_ADMIN_DELETE_ENDPOINT = {
            "/api/users/**"
    };
    static final String[] PRIVATE_ADMIN_PUT_ENDPOINT = {
            "/api/users/**"
    };

    static final String[] PUBLIC_PUT_ENDPOINT = {
            "/api/me/password"
    };


    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/v3/api-docs/public-apis"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                request ->
                        request
                                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINT)
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, PRIVATE_ADMIN_GET_ENDPOINT)
                                .hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, PRIVATE_ADMIN_DELETE_ENDPOINT)
                                .hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, PRIVATE_ADMIN_PUT_ENDPOINT)
                                .hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, PUBLIC_PUT_ENDPOINT)
                                .permitAll()
                                .requestMatchers(SWAGGER_WHITELIST)
                                .permitAll()
                                .anyRequest()
                                .authenticated());
        http.oauth2ResourceServer(
                        oauth2 ->
                                oauth2
                                        .jwt(
                                                jwtConfigurer ->
                                                        jwtConfigurer
                                                                .decoder(customJwtDecoder)
                                                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .exceptionHandling(handler -> handler.accessDeniedHandler(restAccessDeniedHandler));
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Chỉ cho phép origin của front-end
        configuration.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:8081"));
        // Cho phép tất cả phương thức HTTP (GET, POST, PUT, DELETE…)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // Cho phép tất cả header
        configuration.setAllowedHeaders(List.of("*"));
        // Bật gửi cookie/authorization headers
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
                new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
