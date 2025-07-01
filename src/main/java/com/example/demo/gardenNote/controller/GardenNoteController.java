// src/main/java/com/example/demo/gardenNote/controllers/GardenNoteController.java
package com.example.demo.gardenNote.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.annotation.positiveOrDefault.PositiveOrDefault;
import com.example.demo.gardenNote.controller.dtos.CreateGardenNoteRequest;
import com.example.demo.gardenNote.controller.dtos.UpdateGardenNoteRequest;
import com.example.demo.gardenNote.controller.dtos.GardenNoteResponse;
import com.example.demo.gardenNote.service.IGardenNoteService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/garden-notes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenNoteController {

    IGardenNoteService gardenNoteService;

    /**
     * Get paged list of garden notes with optional filters.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<GardenNoteResponse>>> findAll(
            @PositiveOrDefault int page,
            @PositiveOrDefault(defaultValue = 10) int size,
            @RequestParam(required = false) String gardenId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String textContains,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime fromCreatedAt,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime toCreatedAt,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        ApiResponse<List<GardenNoteResponse>> response = gardenNoteService.findAll(
                page, size,
                gardenId,
                userId,
                textContains,
                fromCreatedAt,
                toCreatedAt,
                sortBy,
                sortDir
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get all garden notes for the current authenticated user.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<GardenNoteResponse>>> findAllForCurrentUser() {
        ApiResponse<List<GardenNoteResponse>> response = gardenNoteService.findAllForCurrentUser();
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single garden note by ID.
     */
    @GetMapping("/{noteId}")
    public ResponseEntity<ApiResponse<GardenNoteResponse>> findOneById(
            @PathVariable String noteId
    ) {
        ApiResponse<GardenNoteResponse> response = gardenNoteService.findGardenNoteById(noteId);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new garden note.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createGardenNote(
            @RequestBody @Valid CreateGardenNoteRequest request
    ) {
        ApiResponse<Void> response = gardenNoteService.createGardenNote(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing garden note.
     */
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateGardenNote(
            @RequestBody @Valid UpdateGardenNoteRequest request
    ) {
        ApiResponse<Void> response = gardenNoteService.updateGardenNote(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete one or more garden notes by ID.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteGardenNotes(
            @RequestBody @Valid List<String> noteIds
    ) {
        ApiResponse<Void> response = gardenNoteService.deleteGardenNotes(noteIds);
        return ResponseEntity.ok(response);
    }
}
