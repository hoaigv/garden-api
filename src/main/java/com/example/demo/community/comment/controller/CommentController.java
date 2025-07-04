package com.example.demo.community.comment.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.annotation.positiveOrDefault.PositiveOrDefault;
import com.example.demo.community.comment.controller.dtos.*;
import com.example.demo.community.comment.service.ICommentService;
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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    ICommentService commentService;

    /**
     * Get paged list of comments with optional filters (Admin).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> findAll(
            @PositiveOrDefault int page,
            @PositiveOrDefault(defaultValue = 10) int size,
            @RequestParam(required = false) String postId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTo,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        var response = commentService.findAll(
                page, size, postId, userId, content, createdFrom, createdTo, sortBy, sortDir
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get all comments for a specific post.
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> findByPost(
            @PathVariable String postId
    ) {
        var response = commentService.findByPost(postId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single comment by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> findById(
            @PathVariable String id
    ) {
        var response = commentService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new comment.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @RequestBody @Valid CommentCreateRequest request
    ) {
        var response = commentService.createComment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing comment.
     */
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateComment(
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        var response = commentService.updateComment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete one or more comments by ID.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteComments(
            @RequestBody @Valid CommentDeleteRequest request
    ) {
        var response = commentService.deleteComments(request);
        return ResponseEntity.ok(response);
    }
}
