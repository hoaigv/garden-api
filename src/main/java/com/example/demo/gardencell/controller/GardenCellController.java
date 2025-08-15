package com.example.demo.gardencell.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.gardencell.controller.dtos.*;
import com.example.demo.gardencell.service.IGardenCellService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenCellController {

    IGardenCellService gardenCellService;

    @GetMapping("/cells")
    public ResponseEntity<ApiResponse<GardenCellsViewResponse>> findAll(
            @RequestParam String gardenId
    ) {
        ApiResponse<GardenCellsViewResponse> response = gardenCellService.findAll(
                gardenId
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cells-admin")
    public ResponseEntity<ApiResponse<GardenCellsAdminResponse>> adminFindAll(
            @RequestParam String gardenId,
            @RequestParam(required = false) String plantInventoryId,
            @RequestParam(required = false) String status
    ) {
        var response = gardenCellService.adminFindAll(
                gardenId, plantInventoryId, status
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("{gardenId}/cells")
    public ResponseEntity<ApiResponse<Void>> createCellsBatch(
            @PathVariable String gardenId,
            @Valid @RequestBody BatchCreateGardenCellsRequest batchRequest) {

        var created = gardenCellService.createCellsBatch(gardenId, batchRequest.getCells());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/cells")
    public ResponseEntity<ApiResponse<Void>> updateCell(
            @RequestBody @Valid UpdateGardenCellRequest request
    ) {
        ApiResponse<Void> response = gardenCellService.updateCell(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cells")
    public ResponseEntity<ApiResponse<Void>> deleteCells(
            @RequestBody @Valid DeleteGardenCellsRequest request
    ) {
        ApiResponse<Void> response = gardenCellService.deleteCells(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cells/batch")
    public ResponseEntity<ApiResponse<Void>> updateCellsBatch(
            @RequestBody @Valid UpdateGardenCellsRequest request
    ) {
        ApiResponse<Void> response = gardenCellService.updateCellsBatch(request);
        return ResponseEntity.ok(response);
    }

}
