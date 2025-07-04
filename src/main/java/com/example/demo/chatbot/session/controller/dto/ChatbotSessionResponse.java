package com.example.demo.chatbot.session.controller.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * DTO for returning ChatbotSession details in responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatbotSessionResponse {
    /**
     * Internal database ID (UUID)
     */
    String id;

    /**
     * Title of the chat session
     */
    String chatTitle;


    LocalDateTime createAt;


}
