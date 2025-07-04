package com.example.demo.community.comment.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.community.comment.controller.dtos.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ICommentService {

    /**
     * Retrieve a paged and filtered list of comments.
     *
     * @param page        zero-based page index
     * @param size        page size
     * @param postId      optional filter by post ID
     * @param userId      optional filter by user ID
     * @param content     optional filter by content (contains)
     * @param createdFrom optional filter for comments created after this timestamp
     * @param createdTo   optional filter for comments created before this timestamp
     * @param sortBy      field to sort by
     * @param sortDir     sort direction ("asc" or "desc")
     * @return ApiResponse containing list of CommentResponse
     */
    ApiResponse<List<CommentResponse>> findAll(
            Integer page,
            Integer size,
            String postId,
            String userId,
            String content,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            String sortBy,
            String sortDir
    );

    /**
     * Retrieve all comments belonging to a specific post.
     *
     * @param postId the post ID
     * @return ApiResponse containing list of CommentResponse
     */
    ApiResponse<List<CommentResponse>> findByPost(String postId);

    /**
     * Retrieve a single comment by its ID.
     *
     * @param id the comment ID
     * @return ApiResponse containing CommentResponse
     */
    ApiResponse<CommentResponse> findById(String id);

    /**
     * Create a new comment.
     *
     * @param request the create request DTO
     * @return ApiResponse containing created CommentResponse
     */
    ApiResponse<CommentResponse> createComment(CommentCreateRequest request);

    /**
     * Update an existing comment.
     *
     * @param request the update request DTO
     * @return ApiResponse<Void>
     */
    ApiResponse<Void> updateComment(CommentUpdateRequest request);

    /**
     * Delete one or more comments by their IDs.
     *
     * @param request the delete request DTO
     * @return ApiResponse<Void>
     */
    ApiResponse<Void> deleteComments(CommentDeleteRequest request);
}
