package com.example.demo.chatbot.log.controller.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for returning ChatbotSession details in responses
 */
@Data

@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatbotLogResponse {
    /**
     * Internal database ID (UUID)
     */
    String id;


    String userMessage;


    String botResponse;


}
