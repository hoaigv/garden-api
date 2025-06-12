package com.example.demo.user.controllers.dtos;

import com.example.demo.authentication.controllers.dtos.interf.PasswordConfirmation;
import com.example.demo.common.annotation.passwordMatches.PasswordMatches;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@PasswordMatches
public class PassUpdateRequest implements PasswordConfirmation {

    @NotNull(message = "Old Password mustn't null")
    String oldPassword;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    String password;

    @NotBlank(message = "Confirm Password must not be empty")
    String confirmPassword;

}
