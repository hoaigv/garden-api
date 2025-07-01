// src/main/java/com/example/demo/gardenNote/service/IGardenNoteService.java
package com.example.demo.gardenNote.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.gardenNote.controller.dtos.CreateGardenNoteRequest;
import com.example.demo.gardenNote.controller.dtos.UpdateGardenNoteRequest;
import com.example.demo.gardenNote.controller.dtos.GardenNoteResponse;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Service interface for managing GardenNote entities.
 */
public interface IGardenNoteService {

    /**
     * Retrieve a paged and optionally filtered list of garden notes.
     *
     * @param page          zero-based page index
     * @param size          page size
     * @param gardenId      optional garden ID filter
     * @param userId        optional user ID filter
     * @param textContains  optional text search within noteText
     * @param fromCreatedAt optional lower bound for createdAt
     * @param toCreatedAt   optional upper bound for createdAt
     * @param sortBy        field to sort by
     * @param sortDir       sort direction ("asc" or "desc")
     */
    ApiResponse<List<GardenNoteResponse>> findAll(
            Integer page,
            Integer size,
            String gardenId,
            String userId,
            String textContains,
            OffsetDateTime fromCreatedAt,
            OffsetDateTime toCreatedAt,
            String sortBy,
            String sortDir
    );

    /**
     * Retrieve the full list of garden notes created by the currently authenticated user.
     *
     * @return ApiResponse containing list of GardenNoteResponse for current user
     */
    ApiResponse<List<GardenNoteResponse>> findAllForCurrentUser();

    /**
     * Create a new garden note.
     *
     * @param request DTO containing noteText, photoUrl, gardenId, userId
     */
    ApiResponse<Void> createGardenNote(CreateGardenNoteRequest request);

    /**
     * Update an existing garden note.
     *
     * @param request DTO containing id and updatable fields (noteText, photoUrl)
     */
    ApiResponse<Void> updateGardenNote(UpdateGardenNoteRequest request);

    /**
     * Delete one or more garden notes by their IDs.
     *
     * @param noteIds list of garden note IDs to delete
     */
    ApiResponse<Void> deleteGardenNotes(List<String> noteIds);

    /**
     * Retrieve a single garden note by its ID.
     *
     * @param noteId the ID of the garden note
     * @return ApiResponse containing the GardenNoteResponse
     */
    ApiResponse<GardenNoteResponse> findGardenNoteById(String noteId);
}
