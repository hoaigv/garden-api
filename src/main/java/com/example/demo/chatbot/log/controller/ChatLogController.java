package com.example.demo.chatbot.log.controller;

import com.example.demo.chatbot.log.controller.dtos.ChatbotLogResponse;
import com.example.demo.chatbot.log.service.IChatLogService;
import com.example.demo.common.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot-logs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatLogController {
    IChatLogService chatLogService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<ChatbotLogResponse>>> findLogsById(
            @PathVariable String id
    ) {
        var response = chatLogService.getChatLogsById(id);
        return ResponseEntity.ok(response);
    }
}
