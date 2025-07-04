package com.example.demo.garden.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.annotation.positiveOrDefault.PositiveOrDefault;
import com.example.demo.garden.controller.dtos.*;
import com.example.demo.garden.service.IGardenService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gardens")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenController {

    IGardenService gardenService;

    /**
     * Get paged list of gardens with optional filters (Admin).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<GardenAdminResponse>>> findAll(
            @PositiveOrDefault int page,
            @PositiveOrDefault(defaultValue = 10) int size,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) Integer minRows,
            @RequestParam(required = false) Integer maxRows,
            @RequestParam(required = false) Integer minCols,
            @RequestParam(required = false) Integer maxCols,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        var response = gardenService.findAll(
                page, size, userId, name, condition, minRows, maxRows, minCols, maxCols, sortBy, sortDir
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get list of gardens for current authenticated user.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<GardenResponse>>> findAllForCurrentUser() {
        ApiResponse<List<GardenResponse>> response = gardenService.findAllForCurrentUser();
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single garden by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GardenResponse>> findById(
            @PathVariable String id
    ) {
        ApiResponse<GardenResponse> response = gardenService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new garden.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<GardenResponse>> createGarden(
            @RequestBody @Valid CreateGardenRequest request
    ) {
        var response = gardenService.createGarden(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing garden.
     */
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateGarden(
            @RequestBody @Valid UpdateGardenRequest request
    ) {
        ApiResponse<Void> response = gardenService.updateGarden(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete one or more gardens by ID.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> updatePublicStatus(
            @RequestBody @Valid DeleteGardensRequest request
    ) {
        ApiResponse<Void> response = gardenService.deleteGardens(request);
        return ResponseEntity.ok(response);
    }
}
