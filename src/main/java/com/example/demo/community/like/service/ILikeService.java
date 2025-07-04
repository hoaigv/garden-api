package com.example.demo.community.like.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.community.like.controller.dtos.*;

import java.util.List;

public interface ILikeService {

    /**
     * Retrieve a paged list of likes (for admin).
     * @param page zero-based page index
     * @param size page size
     */
    ApiResponse<List<LikeResponse>> findAll(int page, int size);

    /**
     * Retrieve all likes for a specific post.
     */
    ApiResponse<List<LikeResponse>> findByPost(String postId);

    /**
     * Retrieve all likes by the current user.
     */
    ApiResponse<List<LikeResponse>> findByCurrentUser();

    /**
     * Retrieve a single like by its ID.
     */
    ApiResponse<LikeResponse> findById(String id);

    /**
     * Create a new like.
     */
    ApiResponse<Void> createLike(LikeCreateRequest request);

    /**
     * Delete one or more likes by ID.
     */
    ApiResponse<Void> unLikePost(String id);
}