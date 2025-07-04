package com.example.demo.community.like.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.annotation.positiveOrDefault.PositiveOrDefault;
import com.example.demo.community.like.controller.dtos.*;
import com.example.demo.community.like.service.ILikeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeController {

    ILikeService likeService;

    /**
     * Get paged list of likes (admin scope).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<LikeResponse>>> findAll(
            @PositiveOrDefault int page,
            @PositiveOrDefault(defaultValue = 10) int size
    ) {
        ApiResponse<List<LikeResponse>> response = likeService.findAll(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all likes for a specific post.
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<List<LikeResponse>>> findByPost(
            @PathVariable String postId
    ) {
        ApiResponse<List<LikeResponse>> response = likeService.findByPost(postId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all likes for the current authenticated user.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<LikeResponse>>> findByCurrentUser() {
        ApiResponse<List<LikeResponse>> response = likeService.findByCurrentUser();
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single like by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LikeResponse>> findById(
            @PathVariable String id
    ) {
        ApiResponse<LikeResponse> response = likeService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new like.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createLike(
            @RequestBody @Valid LikeCreateRequest request
    ) {
        var response = likeService.createLike(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete one or more likes by ID.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLikes(
            @PathVariable String id
    ) {
        ApiResponse<Void> response = likeService.unLikePost(id);
        return ResponseEntity.ok(response);
    }
}
