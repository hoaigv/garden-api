package com.example.demo.community.post.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.annotation.positiveOrDefault.PositiveOrDefault;
import com.example.demo.community.post.controller.dtos.*;
import com.example.demo.community.post.service.IPostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    IPostService postService;

    /**
     * Get paged list of posts with optional filters (Admin).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminCommunityPostResponse>>> findAll(
            @PositiveOrDefault int page,
            @PositiveOrDefault(defaultValue = 10) int size,
            @RequestParam(required = false) String userId,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTo,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        var response = postService.findAll(
                page, size, userId, createdFrom, createdTo, sortBy, sortDir
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<CommunityPostResponse>>> findAllForCurrentUser(
            @RequestParam(required = false) Boolean isComment,
            @RequestParam(required = false) Boolean isLike,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTo

    ) {
        ApiResponse<List<CommunityPostResponse>> response =
                postService.findAllForCurrentUser(isComment, isLike, createdFrom, createdTo);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single post by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommunityPostResponse>> findById(
            @PathVariable String id
    ) {
        ApiResponse<CommunityPostResponse> response = postService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new post.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CommunityPostResponse>> createPost(
            @RequestBody @Valid CommunityPostCreateRequest request
    ) {
        var response = postService.createPost(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing post.
     */
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @RequestBody @Valid CommunityPostUpdateRequest request
    ) {
        ApiResponse<Void> response = postService.updatePost(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete one or more posts by ID.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deletePosts(
            @RequestBody @Valid CommunityPostDeleteRequest request
    ) {
        ApiResponse<Void> response = postService.deletePosts(request);
        return ResponseEntity.ok(response);
    }
}
