package com.example.demo.chatbot.session.service;

import com.example.demo.chatbot.session.controller.dto.AdminChatbotSessionResponse;
import com.example.demo.chatbot.session.controller.dto.ChatbotSessionResponse;
import com.example.demo.chatbot.session.controller.dto.ChatbotSessionUpdateRequest;
import com.example.demo.chatbot.session.controller.dto.DeleteChatbotSessionRequest;
import com.example.demo.common.ApiResponse;


import java.util.List;

/**
 * Service interface for managing Chatbot Sessions, similar to garden service patterns.
 */
public interface IChatbotSessionService {

    /**
     * Retrieve a paged and optionally filtered list of chatbot sessions (admin only).
     *
     * @param page    zero-based page index
     * @param size    page size
     * @param sortBy  field to sort by
     * @param sortDir sort direction ("asc" or "desc")
     * @return ApiResponse containing list of ChatbotSessionResponse
     */
    ApiResponse<List<AdminChatbotSessionResponse>> findAll(
            Integer page,
            Integer size,

            String sortBy,
            String sortDir
    );

    /**
     * Retrieve the full list of chatbot sessions for the currently authenticated user.
     * No pagination or filtering by userId param – userId is taken from security context.
     *
     * @return ApiResponse containing list of ChatbotSessionResponse
     */
    ApiResponse<List<ChatbotSessionResponse>> findAllForCurrentUser();

    /**
     * Retrieve a single chatbot session by its ID.
     *
     * @param id session ID
     * @return ApiResponse containing ChatbotSessionResponse
     */
    ApiResponse<ChatbotSessionResponse> findById(String id);

    /**
     * Create a new chatbot session.
     *
     * @return ApiResponse containing created ChatbotSessionResponse
     */
    ApiResponse<ChatbotSessionResponse> createSession();


    /**
     * Delete one or more chatbot sessions by ID.
     *
     * @param request delete DTO (can include multiple IDs if extended)
     * @return ApiResponse with no content
     */
    ApiResponse<Void> deleteSessions(DeleteChatbotSessionRequest request);


    ApiResponse<ChatbotSessionResponse> updateTitleSession(ChatbotSessionUpdateRequest request);
}
