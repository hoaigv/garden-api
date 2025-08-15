package com.example.demo.plantStage.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.plantStage.controller.dtos.CreatePlantStageRequest;
import com.example.demo.plantStage.controller.dtos.DeleteStagesRequest;
import com.example.demo.plantStage.controller.dtos.UpdatePlantStageRequest;
import com.example.demo.plantStage.service.impl.PlantStageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plant-stage")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlantStageController {
    PlantStageService plantStageService;

    @PostMapping
    ResponseEntity<ApiResponse<Void>> createStage(@RequestBody @Valid CreatePlantStageRequest request

    ) {
        var resp = plantStageService.CreateStage(request);
        return ResponseEntity.ok(resp);
    }

    @PutMapping
    ResponseEntity<ApiResponse<Void>> updateStage(@RequestBody @Valid UpdatePlantStageRequest request
    ) {
        var resp = plantStageService.updateStage(request);
        return ResponseEntity.ok(resp);
    }
    @DeleteMapping
    ResponseEntity<ApiResponse<Void>> deleteStage(@RequestBody @Valid DeleteStagesRequest request) {
        var resp = plantStageService.DeleteStages(request);
        return ResponseEntity.ok(resp);
    }
}
