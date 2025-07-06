package com.example.demo.config.security;


import com.example.demo.common.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
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

public class SecurityConfig {
    @Autowired
    private CustomJwtDecoder customJwtDecoder;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    // ===================== PUBLIC (Chưa đăng nhập) =====================
    static final String[] PUBLIC_POST_ENDPOINT = {
            "/api/auth/google",
            "/api/auth/email",
            "/api/auth/register",
            "/api/auth/refresh"
    };

    // ===================== USER (Đã đăng nhập - USER, ADMIN đều được) =====================
    static final String[] USER_GET_ENDPOINT = {
            // Gardens
            "/api/gardens/me",
            "/api/gardens/*",
            // Garden Cells
            "/api/*/cells",
            // Plant Inventories
            "/api/plant-inventories/me",
            // Reminders
            "/api/reminders",        // list + filter
            "/api/reminders/*",      // get by id
            // Garden Notes
            "/api/garden-notes",
            "/api/garden-notes/me",
            "/api/garden-notes/*",
            // Chatbot Sessions
            "/api/chatbot-sessions/me",
            "/api/chatbot-sessions/*",
            "/api/chatbot-logs/*",
            // Community Posts
            "/api/posts/me",
            "/api/posts/*",
            "/api/posts",
            // Comments
            "/api/comments",
            "/api/comments/post/*",
            "/api/comments/*",
            // Likes
            "/api/likes",
            "/api/likes/me",
            "/api/likes/post/*",
            "/api/likes/*",
            // post
            "/api/post"
    };

    static final String[] USER_POST_ENDPOINT = {
            // Authentication
            "/api/auth/logout",
            // File upload
            "/api/files",
            // Gardens
            "/api/gardens",
            // Garden Cells
            "/api/cells",
            // Plant Inventories
            "/api/plant-inventories",
            // Reminders
            "/api/reminders",
            // Garden Notes
            "/api/garden-notes",
            // Chatbot Sessions
            "/api/chatbot-sessions/new-chat",
            // Community Posts
            "/api/posts",
            // Comments
            "/api/comments",
            // Likes
            "/api/likes"
    };

    static final String[] USER_PUT_ENDPOINT = {
            // Gardens
            "/api/gardens",
            // Garden Cells
            "/api/cells",
            // Plant Inventories
            "/api/plant-inventories",
            // Change own password
            "/api/me/password",
            // Reminders
            "/api/reminders/*",  // update by id
            // Garden Notes
            "/api/garden-notes",
            // Chatbot Sessions
            "/api/chatbot-sessions",
            // Community Posts
            "/api/posts",
            // Comments
            "/api/comments",
            // Likes
            "/api/likes"
    };

    static final String[] USER_DELETE_ENDPOINT = {
            // Gardens
            "/api/gardens",
            // Garden Cells
            "/api/cells",
            // Plant Inventories
            "/api/plant-inventories",
            // Reminders
            "/api/reminders/*",  // delete by id
            // Garden Notes
            "/api/garden-notes",
            // Chatbot Sessions
            "/api/chatbot-sessions",
            // Community Posts
            "/api/posts",
            // Comments
            "/api/comments",
            // Likes
            "/api/likes/*"
    };

    // ===================== ADMIN (Chỉ dành cho ADMIN) =====================
    static final String[] ADMIN_GET_ENDPOINT = {
            // All Gardens
            "/api/gardens",
            "/api/cells-admin",
            // All Plant Inventories
            "/api/plant-inventories",
            // User management
            "/api/users",
            // Chatbot Sessions (full list)
            "/api/chatbot-sessions",
            // Community Posts
            "/api/posts",
            // Comments
            "/api/comments",
            "/api/comments/post/*",
            "/api/comments/*",
            // Likes
            "/api/likes",
            "/api/likes/me",
            "/api/likes/post/*",
            "/api/likes/*"
    };

    static final String[] ADMIN_POST_ENDPOINT = {
            // (hiện tại không có POST chỉ dành cho admin)
    };

    static final String[] ADMIN_PUT_ENDPOINT = {
            // Update user roles
            "/api/users"
    };

    static final String[] ADMIN_DELETE_ENDPOINT = {
            // Delete users
            "/api/users",
            // Chatbot Sessions
            "/api/chatbot-sessions",
            // Community Posts
            "/api/posts",
            // Comments
            "/api/comments",
            // Likes
            "/api/likes"
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
                                // ---- Public ----
                                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINT).permitAll()
                                // ---- USER (authenticated) ----
                                .requestMatchers(HttpMethod.GET, USER_GET_ENDPOINT).authenticated()
                                .requestMatchers(HttpMethod.POST, USER_POST_ENDPOINT).authenticated()
                                .requestMatchers(HttpMethod.PUT, USER_PUT_ENDPOINT).authenticated()
                                .requestMatchers(HttpMethod.DELETE, USER_DELETE_ENDPOINT).authenticated()
                                // ---- ADMIN-only ----
                                .requestMatchers(HttpMethod.GET, ADMIN_GET_ENDPOINT).hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST, ADMIN_POST_ENDPOINT).hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, ADMIN_PUT_ENDPOINT).hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, ADMIN_DELETE_ENDPOINT).hasAuthority(Role.ADMIN.name())
                                // ---- Swagger/OpenAPI ----
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
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:8081"));
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
