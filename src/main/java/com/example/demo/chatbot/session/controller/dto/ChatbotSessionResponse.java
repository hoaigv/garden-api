package com.example.demo.chatbot.session.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning ChatbotSession details in responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatbotSessionResponse {
    /**
     * Internal database ID (UUID)
     */
    private String id;

    /**
     * Title of the chat session
     */
    private String chatTitle;


    private LocalDateTime createAt;


}
