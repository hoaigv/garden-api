package com.example.demo.community.post.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.community.comment.repository.CommentSpecification;
import com.example.demo.community.comment.repository.ICommentRepository;
import com.example.demo.community.like.repository.LikeRepository;
import com.example.demo.community.like.repository.LikeSpecification;
import com.example.demo.community.post.controller.dtos.*;
import com.example.demo.community.post.mapper.IPostMapper;
import com.example.demo.community.post.model.CommunityPostEntity;
import com.example.demo.community.post.repository.IPostRepository;
import com.example.demo.community.post.repository.PostSpecification;
import com.example.demo.community.post.service.IPostService;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements IPostService {

    IPostRepository postRepository;
    IPostMapper postMapper;
    IUserRepository userRepository;
    LikeRepository likeRepository;
    ICommentRepository commentRepository;

    @Override
    public ApiResponse<List<AdminCommunityPostResponse>> findAll(
            Integer page,
            Integer size,
            String userId,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            String sortBy,
            String sortDir
    ) {
        Specification<CommunityPostEntity> spec = PostSpecification.build(
                null, userId, createdFrom, createdTo
        );
        Sort.Direction dir = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        Page<CommunityPostEntity> pageResult = postRepository.findAll(spec, pageable);


        var userCurrent = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        var userCId = userCurrent.getId();

        List<AdminCommunityPostResponse> result = new ArrayList<>();
        for (CommunityPostEntity communityPostEntity : pageResult.getContent()) {
            var postResponse = postMapper.entityToResponse(communityPostEntity);
            var totalLike = likeRepository.count(LikeSpecification.hasPostId(communityPostEntity.getId()));
            var totalComment = commentRepository.count(CommentSpecification.hasPostId(communityPostEntity.getId()));
            var isUserLike = likeRepository.findOne(LikeSpecification.hasUserId(userCId).and(LikeSpecification.hasPostId(communityPostEntity.getId())));
            if (isUserLike.isPresent()) {
                postResponse.setIsLike(true);
            } else {
                postResponse.setIsLike(false);
            }
            var userLike = userRepository.findOne(UserSpecification.hasEmail(communityPostEntity.getUser().getEmail()))
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
            postResponse.setUserName(userLike.getName());
            postResponse.setUserLink(userLike.getAvatarLink());
            postResponse.setTotalComment(totalComment);
            postResponse.setTotalLike(totalLike);
            var apply = AdminCommunityPostResponse.builder()
                    .communityPostResponse(postResponse)
                    .deleteAt(communityPostEntity.getDeletedAt())
                    .updateAt(communityPostEntity.getUpdatedAt())
                    .build();

            result.add(apply);
        }

        return ApiResponse.<List<AdminCommunityPostResponse>>builder()
                .message("Successfully fetched posts")
                .result(result)
                .total(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<List<CommunityPostResponse>> findAllForCurrentUser(
            Boolean isComment,
            Boolean isLike,
            String createdFrom,
            String createdTo
    ) {
        // Lấy người dùng hiện tại từ Security Context
        String email = AuthUtils.getUserCurrent();
        UserEntity currentUser = userRepository.findOne(
                UserSpecification.hasEmail(email)
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));


        // Build Specification
        Specification<CommunityPostEntity> spec = PostSpecification.buildCurrent(
                currentUser.getId(), isComment, isLike
        );

        // Fetch danh sách bài viết
        List<CommunityPostEntity> posts = postRepository.findAll(spec.and(PostSpecification.isNotDelete()));

        // Mapping sang DTO
        List<CommunityPostResponse> dtos = posts.stream()
                .map(postMapper::entityToResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<CommunityPostResponse>>builder()
                .message("Fetched posts for current user")
                .result(dtos)
                .build();
    }


    @Override
    public ApiResponse<CommunityPostResponse> findById(String id) {
        CommunityPostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        CommunityPostResponse response = postMapper.entityToResponse(entity);
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));


        var totalLike = likeRepository.count(LikeSpecification.hasPostId(entity.getId()));
        var totalComment = commentRepository.count(CommentSpecification.hasPostId(entity.getId()));
        var isUserLike = likeRepository.findOne(LikeSpecification.hasUserId(user.getId()).and(LikeSpecification.hasPostId(entity.getId())));
        if (isUserLike.isPresent()) {
            response.setIsLike(true);
        } else {
            response.setIsLike(false);
        }
        var userLike = userRepository.findOne(UserSpecification.hasId(entity.getUser().getId()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        var userName = userLike.getName();
        var userLink = userLike.getAvatarLink();
        response.setUserName(userName);
        response.setUserLink(userLink);
        response.setTotalComment(totalComment);
        response.setTotalLike(totalLike);
        return ApiResponse.<CommunityPostResponse>builder()
                .message("Successfully fetched post")
                .result(response)
                .build();
    }

    @Override
    public ApiResponse<CommunityPostResponse> createPost(CommunityPostCreateRequest request) {
        CommunityPostEntity entity = postMapper.createRequestToEntity(request);
        UserEntity user = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        entity.setUser(user);
        entity = postRepository.save(entity);
        CommunityPostResponse response = postMapper.entityToResponse(entity);
        return ApiResponse.<CommunityPostResponse>builder()
                .message("Post created successfully")
                .result(response)
                .build();
    }

    @Override
    public ApiResponse<Void> updatePost(CommunityPostUpdateRequest request) {
        CommunityPostEntity existing = postRepository.findById(request.getId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        postMapper.updateEntityFromRequest(request, existing);
        postRepository.save(existing);
        return ApiResponse.<Void>builder()
                .message("Post updated successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> deletePosts(CommunityPostDeleteRequest request) {
        for (String id : request.getIds()) {
            CommunityPostEntity entity = postRepository.findById(id)
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
            if (entity.getDeletedAt() == null) {
                entity.setDeletedAt(LocalDateTime.now());
                postRepository.save(entity);
            } else {
                entity.setDeletedAt(null);
                postRepository.save(entity);
            }
        }
        return ApiResponse.<Void>builder()
                .message("Posts deleted successfully")
                .build();
    }
}
