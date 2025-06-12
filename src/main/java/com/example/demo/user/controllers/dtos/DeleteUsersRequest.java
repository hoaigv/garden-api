package com.example.demo.user.controllers.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteUsersRequest {
    @NotEmpty(message = "List of user ID don't empty !")
    @NotNull(message = "List of user ID don't null")
    private List<String> ids;
}
