package com.example.demo.chatbot.session.service.impl;

import com.example.demo.chatbot.log.controller.dtos.ChatbotLogResponse;
import com.example.demo.chatbot.log.repository.ChatLogSpecification;
import com.example.demo.chatbot.log.repository.IChatbotLogRepository;
import com.example.demo.chatbot.session.controller.dto.ChatbotSessionResponse;
import com.example.demo.chatbot.session.controller.dto.ChatbotSessionUpdateRequest;
import com.example.demo.chatbot.session.controller.dto.DeleteChatbotSessionRequest;
import com.example.demo.chatbot.session.model.ChatbotSessionEntity;
import com.example.demo.chatbot.session.repository.IChatbotSessionRepository;
import com.example.demo.chatbot.session.service.IChatbotSessionService;
import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.user.repository.IUserRepository;
import com.example.demo.user.repository.UserSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatbotSessionService implements IChatbotSessionService {
    IUserRepository userRepository;
    IChatbotSessionRepository chatbotSessionRepository;
    IChatbotLogRepository chatbotLogRepository;

    @Override
    public ApiResponse<List<ChatbotSessionResponse>> findAll(Integer page, Integer size, String userId, String chatTitle, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public ApiResponse<List<ChatbotSessionResponse>> findAllForCurrentUser() {
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        var sessions = chatbotSessionRepository.findAllByUser_Id(user.getId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));

        var res = sessions.stream().map(
                (session) -> ChatbotSessionResponse.builder()
                        .chatTitle(session.getChatTile())
                        .id(session.getId())
                        .createAt(session.getCreatedAt())
                        .build()
        ).toList();

        return ApiResponse.<List<ChatbotSessionResponse>>builder()
                .result(res)
                .build();
    }

    @Override
    public ApiResponse<ChatbotSessionResponse> findById(String id) {
        return null;
    }

    @Override
    public ApiResponse<ChatbotSessionResponse> createSession() {
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        var entity = ChatbotSessionEntity.builder()
                .chatTile("Chat_box_" + UUID.randomUUID().toString().substring(0, 6))
                .user(user)
                .build();
        var session_new = chatbotSessionRepository.save(entity);
        var res = ChatbotSessionResponse.builder()
                .chatTitle(session_new.getChatTile())
                .id(session_new.getId())
                .createAt(session_new.getCreatedAt())
                .build();
        return ApiResponse.<ChatbotSessionResponse>builder()
                .result(res)
                .build();
    }

    @Override
    public ApiResponse<ChatbotSessionResponse> updateTitleSession(ChatbotSessionUpdateRequest request) {
        var session = chatbotSessionRepository.findAllById(request.getId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        session.setChatTile(request.getChatTitle());
        var entity = chatbotSessionRepository.save(session);
        var res = ChatbotSessionResponse.builder()
                .chatTitle(entity.getChatTile())
                .id(entity.getId())
                .createAt(entity.getCreatedAt())
                .build();
        return ApiResponse.<ChatbotSessionResponse>builder()
                .message("Update Chat Room Title Successfully!")
                .result(res)
                .build();
    }

    @Override
    public ApiResponse<Void> deleteSessions(DeleteChatbotSessionRequest request) {
        for (String id : request.getIds()) {
            var listLogs = chatbotLogRepository.findAll(ChatLogSpecification.hasSessionId(id))
                    .stream().toList();
            chatbotLogRepository.deleteAll(listLogs);

            chatbotSessionRepository.deleteById(id);

        }
        return ApiResponse.<Void>builder()
                .message("Delete Session Successfully!")
                .build();
    }
}
