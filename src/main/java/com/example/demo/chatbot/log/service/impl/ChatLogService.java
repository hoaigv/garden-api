package com.example.demo.chatbot.log.service.impl;

import com.example.demo.chatbot.log.controller.dtos.ChatbotLogResponse;
import com.example.demo.chatbot.log.repository.ChatLogSpecification;
import com.example.demo.chatbot.log.repository.IChatbotLogRepository;
import com.example.demo.chatbot.log.service.IChatLogService;
import com.example.demo.common.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatLogService implements IChatLogService {
    IChatbotLogRepository chatbotLogRepository;

    @Override
    public ApiResponse<List<ChatbotLogResponse>> getChatLogsById(String id) {
        var logs = chatbotLogRepository.findAll(ChatLogSpecification.hasSessionId(id));

        var res = logs.stream().map(
                log -> ChatbotLogResponse.builder()
                        .id(log.getId())
                        .botResponse(log.getBotResponse())
                        .userMessage(log.getUserMessage())
                        .build()
        ).toList();
        return ApiResponse.<List<ChatbotLogResponse>>builder()
                .result(res)
                .build();
    }
}
