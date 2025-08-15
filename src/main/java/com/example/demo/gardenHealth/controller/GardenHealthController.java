package com.example.demo.gardenHealth.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.gardenHealth.controller.dto.GardenHealthResponse;
import com.example.demo.gardenHealth.service.GardenHealthService;
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
@RequestMapping("/api/garden-health")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenHealthController {
    GardenHealthService gardenHealthService;

    @GetMapping("{id}")
    ResponseEntity<ApiResponse<List<GardenHealthResponse>>> getGardenHealth(@PathVariable String id
    ) {
        var resp = gardenHealthService.getGardenHeath(id);
        return ResponseEntity.ok(resp);
    }
}
