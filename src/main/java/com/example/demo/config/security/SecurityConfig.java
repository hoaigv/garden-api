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

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SecurityConfig {
    CustomJwtDecoder customJwtDecoder;
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    RestAccessDeniedHandler restAccessDeniedHandler;
    static final String[] PUBLIC_POST_ENDPOINT = {
            "/api/auth/signup", "/api/auth/login", "/api/auth/refresh", "/api/files"
    };
    static final String[] GET_ENDPOINT = {"/api/users/me", "/api/users/update"};
    static final String[] PRIVATE_GET_ENDPOINT = {

    };
    static final String[] PRIVATE_ADMIN_POST_ENDPOINT = {"/api/users/register"};
    static final String[] PRIVATE_ADMIN_PUT_ENDPOINT = {"/api/users/*"};
    static final String[] PRIVATE_ADMIN_GET_ENDPOINT = {"/api/users/*","api/doctors/*"};
    static final String[] PRIVATE_ADMIN_DELETE_ENDPOINT = {"/api/users", "/api/address/{id}"};
    static final String[] PRIVATE_DELETE_ENDPOINT = {"/api/address/users/{id}"};
    static final String[] PRIVATE_POST_ENDPOINT = {"/api/address"};
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
                                .requestMatchers(HttpMethod.GET, GET_ENDPOINT)
                                .hasAnyAuthority(Role.USER.toString())
                                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINT)
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, PRIVATE_ADMIN_POST_ENDPOINT)
                                .hasAnyAuthority(Role.ADMIN.toString())
                                .requestMatchers(HttpMethod.PUT, PRIVATE_ADMIN_PUT_ENDPOINT)
                                .hasAnyAuthority(Role.ADMIN.toString())
                                .requestMatchers(HttpMethod.GET, PRIVATE_ADMIN_GET_ENDPOINT)
                                .hasAnyAuthority(Role.ADMIN.toString())
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
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
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
