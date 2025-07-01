package com.example.demo.chatbot.session.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for deleting one or more chatbot sessions via API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteChatbotSessionRequest {



    /**
     * List of chatbot session IDs to delete.
     */
    @NotEmpty(message = "ids must not be empty")
    private List<String> ids;
}
