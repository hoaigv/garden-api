package com.example.demo.chatbot.session.service.impl;

import com.example.demo.chatbot.log.controller.dtos.ChatbotLogResponse;
import com.example.demo.chatbot.log.repository.ChatLogSpecification;
import com.example.demo.chatbot.log.repository.IChatbotLogRepository;
import com.example.demo.chatbot.session.controller.dto.AdminChatbotSessionResponse;
import com.example.demo.chatbot.session.controller.dto.ChatbotSessionResponse;
import com.example.demo.chatbot.session.controller.dto.ChatbotSessionUpdateRequest;
import com.example.demo.chatbot.session.controller.dto.DeleteChatbotSessionRequest;
import com.example.demo.chatbot.session.model.ChatbotSessionEntity;
import com.example.demo.chatbot.session.repository.ChatbotSessionSpecification;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatbotSessionService implements IChatbotSessionService {
    IUserRepository userRepository;
    IChatbotSessionRepository chatbotSessionRepository;
    IChatbotLogRepository chatbotLogRepository;

    @Override
    public ApiResponse<List<AdminChatbotSessionResponse>> findAll(
            Integer page,
            Integer size,
            String sortBy,
            String sortDir
    ) {
        // 1. Không có filter, chỉ lấy tất cả → spec rỗng
        Specification<ChatbotSessionEntity> spec = (root, query, cb) -> cb.conjunction();

        // 2. Thiết lập pageable kèm sort
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // 3. Query DB
        Page<ChatbotSessionEntity> pageResult =
                chatbotSessionRepository.findAll(spec, pageable);

        // 4. Map sang DTO
        List<AdminChatbotSessionResponse> result = pageResult.getContent().stream()
                .map(entity -> AdminChatbotSessionResponse.builder()
                        .chatbotSessionResponse(
                                ChatbotSessionResponse.builder()
                                        .id(entity.getId())
                                        .createAt(entity.getCreatedAt())
                                        .chatTitle(entity.getChatTile())
                                        .build()
                        )
                        .userId(entity.getUser().getId())
                        .updateAt(entity.getCreatedAt())
                        .deletedAt(entity.getDeletedAt())
                        .build()
                )
                .collect(Collectors.toList());

        // 5. Đóng gói ApiResponse
        return ApiResponse.<List<AdminChatbotSessionResponse>>builder()
                .message("Successfully fetched chatbot sessions")
                .result(result)
                .total(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }


    @Override
    public ApiResponse<List<ChatbotSessionResponse>> findAllForCurrentUser() {
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        var sessions = chatbotSessionRepository.findAll(ChatbotSessionSpecification.hasUserId(user.getId())
                .and(ChatbotSessionSpecification.isNotDelete())
        );

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
            var session = chatbotSessionRepository.findById(id)
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
            if (session.getDeletedAt() == null) {
                session.setDeletedAt(LocalDateTime.now());
                chatbotSessionRepository.save(session);
            } else {
                session.setDeletedAt(null);
                chatbotSessionRepository.save(session);
            }

        }
        return ApiResponse.<Void>builder()
                .message("Delete Session Successfully!")
                .build();
    }
}
