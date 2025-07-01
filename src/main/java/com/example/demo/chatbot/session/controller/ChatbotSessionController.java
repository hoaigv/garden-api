package com.example.demo.chatbot.session.controller;

import com.example.demo.chatbot.session.controller.dto.ChatbotSessionResponse;
import com.example.demo.chatbot.session.controller.dto.ChatbotSessionUpdateRequest;
import com.example.demo.chatbot.session.controller.dto.DeleteChatbotSessionRequest;
import com.example.demo.common.ApiResponse;
import com.example.demo.common.annotation.positiveOrDefault.PositiveOrDefault;

import com.example.demo.chatbot.session.service.IChatbotSessionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot-sessions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatbotSessionController {

    IChatbotSessionService sessionService;

    /**
     * Get paged list of chatbot sessions with optional filters (Admin).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatbotSessionResponse>>> findAll(
            @PositiveOrDefault int page,
            @PositiveOrDefault(defaultValue = 10) int size,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String chatTitle,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        ApiResponse<List<ChatbotSessionResponse>> response = sessionService.findAll(
                page, size, userId, chatTitle, sortBy, sortDir
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get list of chatbot sessions for current authenticated user.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<ChatbotSessionResponse>>> findAllForCurrentUser() {
        ApiResponse<List<ChatbotSessionResponse>> response = sessionService.findAllForCurrentUser();
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single chatbot session by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChatbotSessionResponse>> findById(
            @PathVariable String id
    ) {
        ApiResponse<ChatbotSessionResponse> response = sessionService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new chatbot session.
     */
    @PostMapping("new-chat")
    public ResponseEntity<ApiResponse<ChatbotSessionResponse>> createSession(

    ) {
        ApiResponse<ChatbotSessionResponse> response = sessionService.createSession();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ChatbotSessionResponse>> updateSession(
            @RequestBody @Valid ChatbotSessionUpdateRequest updateRequest
    ) {
        var response = sessionService.updateTitleSession(updateRequest);
        return ResponseEntity.ok(response);
    }


    /**
     * Delete one or more chatbot sessions by ID.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteSessions(
            @RequestBody @Valid DeleteChatbotSessionRequest request
    ) {
        ApiResponse<Void> response = sessionService.deleteSessions(request);
        return ResponseEntity.ok(response);
    }
}
