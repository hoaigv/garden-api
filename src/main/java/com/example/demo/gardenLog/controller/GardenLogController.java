package com.example.demo.gardenLog.controller;// --- UPDATED: src/main/java/com/example/demo/gardenLog/controllers/GardenLogController.java ---


import com.example.demo.common.ApiResponse;
import com.example.demo.gardenLog.controller.dto.GardenLogResponse;
import com.example.demo.gardenLog.service.GardenLogService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/garden-logs")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GardenLogController {

    GardenLogService gardenLogService;

    /**
     * Trả về danh sách GardenLogResponse, service đã xử lý mapping
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<GardenLogResponse>>> getLogs(
            @RequestParam String gardenId
    ) {
        List<GardenLogResponse> dtos = gardenLogService.getLogs(gardenId);
        return ResponseEntity.ok(
                ApiResponse.<List<GardenLogResponse>>builder()
                        .result(dtos)
                        .build()
        );
    }
}