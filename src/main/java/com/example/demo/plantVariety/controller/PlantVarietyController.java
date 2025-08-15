package com.example.demo.plantVariety.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.annotation.positiveOrDefault.PositiveOrDefault;
import com.example.demo.plantVariety.controller.dtos.*;
import com.example.demo.plantVariety.service.IPlantVarietyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plant-variety")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlantVarietyController {

    IPlantVarietyService plantVarietyService;

    @PostMapping
    ResponseEntity<ApiResponse<Void>> createPlantVariety(
            @RequestBody @Valid CreatePlantVarietyRequest request
    ) {
        var res = plantVarietyService.CreateVariety(request);

        return ResponseEntity.ok(res);
    }

    @PutMapping("/{varietyId}")
    ResponseEntity<ApiResponse<Void>> updatePlantVariety(
            @PathVariable String varietyId,
            @RequestBody @Valid UpdatePlantVarietyRequest request
    ) {
        var res = plantVarietyService.UpdateVariety(request, varietyId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/type")
    ResponseEntity<ApiResponse<List<PlantVarietyResponse>>> getVarietyByType(
            @RequestParam String type
    ) {
        var res = plantVarietyService.getAllVarieties(type);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{varietyId}")
    ResponseEntity<ApiResponse<VarietyDetailResponse>> getVarietyDetail(
            @PathVariable String varietyId
    ) {
        var res = plantVarietyService.getDetailVarietyDetail(varietyId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/detail/{varietyId}")
    ResponseEntity<ApiResponse<AdminVarietyDetailResponse>> getVarietyByVarietyId(
            @PathVariable String varietyId
    ) {
        var res = plantVarietyService.getAdminVarietyDetail(varietyId);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<AdminVarietyResponse>>> findAll(
            @PositiveOrDefault int page,
            @PositiveOrDefault(defaultValue = 10) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        var resp = plantVarietyService.findAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping
    ResponseEntity<ApiResponse<Void>> deleteVariety(@RequestBody @Valid DeleteGardensRequest request){
        var resp = plantVarietyService.DeleteVariety(request.getIds());
        return ResponseEntity.ok(resp);
    }

}
