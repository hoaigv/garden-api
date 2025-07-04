package com.example.demo.chatbot.session.controller.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminChatbotSessionResponse {
    ChatbotSessionResponse chatbotSessionResponse;
    LocalDateTime deletedAt;
    LocalDateTime updateAt;
    String userId;
}
