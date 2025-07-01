package com.example.demo.chatbot.log.service;

import com.example.demo.chatbot.log.controller.dtos.ChatbotLogResponse;
import com.example.demo.common.ApiResponse;

import java.util.List;

public interface IChatLogService {
    ApiResponse<List<ChatbotLogResponse>> getChatLogsById(String id);
}
