package com.example.demo.user.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUserUpdateRequest {
    @NotBlank(message = "Role don't empty")
    @NotNull(message = "Role don't null")
    @Pattern(
            regexp = "ADMIN|USER",
            message = "Role must be ADMIN or USER"
    )
    String role;
    @NotBlank(message = "Role don't empty")
    @NotNull(message = "Role don't null")
    String id;
}
