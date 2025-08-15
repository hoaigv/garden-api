package com.example.demo.plantInventory.controllers;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.annotation.positiveOrDefault.PositiveOrDefault;
import com.example.demo.plantInventory.controllers.dtos.*;
import com.example.demo.plantInventory.service.IPlantInventoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plant-inventories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlantInventoryController {

    IPlantInventoryService inventoryService;

    /**
     * Get paged list of plant inventories with optional filters.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PlantInventoryAdminResponse>>> findAll(
            @PositiveOrDefault int page,
            @PositiveOrDefault(defaultValue = 10) int size,
            @RequestParam(required = false) String userId,

            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        var response = inventoryService.findAll(
                page, size, userId, sortBy, sortDir
        );
        return ResponseEntity.ok(response);
    }

    // --- new endpoint for current user ---
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<PlantInventoryResponse>>> findAllForCurrentUser() {
        var response = inventoryService.findAllForCurrentUser();
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new plant inventory.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createPlantInventory(
            @RequestBody @Valid CreatePlantInventoryRequest request
    ) {
        ApiResponse<Void> response = inventoryService.createPlantInventory(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing plant inventory.
     */
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updatePlantInventory(
            @RequestBody @Valid UpdatePlantInventoryRequest request
    ) {
        ApiResponse<Void> response = inventoryService.updatePlantInventory(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete one or more plant inventories by ID.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deletePlantInventories(
            @RequestBody @Valid DeletePlantInventoriesRequest request
    ) {
        ApiResponse<Void> response = inventoryService.deletePlantInventories(request);
        return ResponseEntity.ok(response);
    }
}
