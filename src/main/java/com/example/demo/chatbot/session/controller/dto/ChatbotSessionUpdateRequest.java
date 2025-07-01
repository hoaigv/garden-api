package com.example.demo.chatbot.session.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatbotSessionUpdateRequest {
    @NotBlank
    @NotNull
    String chatTitle;

    @NotNull(message = "Session ID must not be null")
    @NotNull(message = "Session Id must not be blank")
    String id;

}
