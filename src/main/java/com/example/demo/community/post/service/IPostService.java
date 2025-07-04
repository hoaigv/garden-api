package com.example.demo.community.post.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.community.post.controller.dtos.*;

import java.time.LocalDateTime;
import java.util.List;

public interface IPostService {

    /**
     * Retrieve a paged and filtered list of community posts.
     *
     * @param page        zero-based page index
     * @param size        page size
     * @param userId      optional owner user ID filter

     * @param createdFrom optional filter for posts created after this timestamp
     * @param createdTo   optional filter for posts created before this timestamp
     * @param sortBy      field to sort by
     * @param sortDir     sort direction ("asc" or "desc")
     * @return ApiResponse containing list of CommunityPostResponse
     */
    ApiResponse<List<AdminCommunityPostResponse>> findAll(
            Integer page,
            Integer size,
            String userId,

            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            String sortBy,
            String sortDir
    );

    /**
     * Retrieve the list of posts by the currently authenticated user,
     * with optional filters provided via method parameters.
     *
     * The userId is taken from the Security Context.
     * Supports filters such as like status, comment status, creation range, and sorting.
     *
     * @param isComment filter by posts the user has commented on
     * @param isLike filter by posts the user has liked
     * @param createdFrom start date for post creation (ISO 8601 format)
     * @param createdTo end date for post creation (ISO 8601 format)

     * @return ApiResponse containing a list of CommunityPostResponse
     */
    ApiResponse<List<CommunityPostResponse>> findAllForCurrentUser(
            Boolean isComment,
            Boolean isLike,
            String createdFrom,
            String createdTo

    );


    /**
     * Retrieve a single post by its ID.
     *
     * @param id the post ID
     * @return ApiResponse containing CommunityPostResponse
     */
    ApiResponse<CommunityPostResponse> findById(String id);

    /**
     * Create a new community post.
     *
     * @param request the create request DTO
     * @return ApiResponse containing created CommunityPostResponse
     */
    ApiResponse<CommunityPostResponse> createPost(CommunityPostCreateRequest request);

    /**
     * Update an existing community post.
     *
     * @param request the update request DTO
     * @return ApiResponse<Void>
     */
    ApiResponse<Void> updatePost(CommunityPostUpdateRequest request);

    /**
     * Delete one or more community posts by their IDs.
     *
     * @param request the delete request DTO
     * @return ApiResponse<Void>
     */
    ApiResponse<Void> deletePosts(CommunityPostDeleteRequest request);
}
