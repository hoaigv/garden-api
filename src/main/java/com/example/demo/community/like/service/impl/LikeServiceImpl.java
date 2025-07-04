package com.example.demo.community.like.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;

import com.example.demo.community.like.controller.dtos.LikeCreateRequest;
import com.example.demo.community.like.controller.dtos.LikeDeleteRequest;
import com.example.demo.community.like.controller.dtos.LikeResponse;
import com.example.demo.community.like.model.LikeEntity;
import com.example.demo.community.like.repository.LikeRepository;
import com.example.demo.community.like.repository.LikeSpecification;
import com.example.demo.community.like.service.ILikeService;
import com.example.demo.community.post.model.CommunityPostEntity;

import com.example.demo.community.post.repository.IPostRepository;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.user.model.UserEntity;
import com.example.demo.user.repository.IUserRepository;
import com.example.demo.user.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeServiceImpl implements ILikeService {

    LikeRepository likeRepository;
    IUserRepository userRepository;
    IPostRepository postRepository;

    @Override
    public ApiResponse<List<LikeResponse>> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LikeEntity> p = likeRepository.findAll(pageable);
        List<LikeResponse> dtos = p.getContent().stream().map(this::toDto).collect(Collectors.toList());
        return ApiResponse.<List<LikeResponse>>builder()
                .message("Fetched likes")
                .result(dtos)
                .total(p.getTotalElements())
                .totalPages(p.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<List<LikeResponse>> findByPost(String postId) {
        List<LikeResponse> dtos = likeRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("post").get("id"), postId)
        ).stream().map(this::toDto).collect(Collectors.toList());
        return ApiResponse.<List<LikeResponse>>builder()
                .message("Fetched likes for post " + postId)
                .result(dtos)
                .build();
    }

    @Override
    public ApiResponse<List<LikeResponse>> findByCurrentUser() {
        String email = AuthUtils.getUserCurrent();
        UserEntity user = userRepository.findOne(
                UserSpecification.hasEmail(email)
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        List<LikeResponse> dtos = likeRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("user").get("id"), user.getId())
        ).stream().map(this::toDto).collect(Collectors.toList());
        return ApiResponse.<List<LikeResponse>>builder()
                .message("Fetched likes for current user")
                .result(dtos)
                .build();
    }

    @Override
    public ApiResponse<LikeResponse> findById(String id) {
        LikeEntity e = likeRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        return ApiResponse.<LikeResponse>builder()
                .message("Fetched like")
                .result(toDto(e))
                .build();
    }

    @Override
    public ApiResponse<Void> createLike(LikeCreateRequest request) {
        // fetch user
        UserEntity user = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        // fetch post
        CommunityPostEntity post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        LikeEntity entity = LikeEntity.builder()
                .user(user)
                .post(post)
                .build();
        likeRepository.save(entity);
        return ApiResponse.<Void>builder()
                .message("Like created")

                .build();
    }

    @Override
    public ApiResponse<Void> unLikePost(String id) {
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        var like = likeRepository.findOne(LikeSpecification.hasPostId(id).and(LikeSpecification.hasUserId(user.getId())))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        likeRepository.delete(like);
        return ApiResponse.<Void>builder()
                .message("Unlike post successfully !")
                .build();
    }

    private LikeResponse toDto(LikeEntity e) {
        return LikeResponse.builder()
                .id(e.getId())
                .postId(e.getPost().getId())
                .userId(e.getUser().getId())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
