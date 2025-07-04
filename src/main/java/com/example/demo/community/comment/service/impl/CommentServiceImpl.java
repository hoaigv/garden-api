package com.example.demo.community.comment.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.community.comment.controller.dtos.*;
import com.example.demo.community.comment.mapper.ICommentMapper;
import com.example.demo.community.comment.model.CommentEntity;
import com.example.demo.community.comment.repository.ICommentRepository;
import com.example.demo.community.comment.repository.CommentSpecification;
import com.example.demo.community.comment.service.ICommentService;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.community.post.model.CommunityPostEntity;
import com.example.demo.community.post.repository.IPostRepository;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements ICommentService {

    ICommentRepository commentRepository;
    ICommentMapper commentMapper;
    IUserRepository userRepository;
    IPostRepository postRepository;

    @Override
    public ApiResponse<List<CommentResponse>> findAll(
            Integer page,
            Integer size,
            String postId,
            String userId,
            String content,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            String sortBy,
            String sortDir
    ) {
        Specification<CommentEntity> spec = CommentSpecification.build(
                null, postId, userId, content, createdFrom, createdTo
        );
        Sort.Direction dir = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        Page<CommentEntity> pageResult = commentRepository.findAll(spec, pageable);

        List<CommentResponse> result = pageResult.getContent().stream()
                .map(commentMapper::entityToResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<CommentResponse>>builder()
                .message("Fetched comments successfully")
                .result(result)
                .total(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<List<CommentResponse>> findByPost(String postId) {
        Specification<CommentEntity> spec = CommentSpecification.build(
                null, postId, null, null, null, null
        );
        List<CommentResponse> dtos = commentRepository.findAll(spec).stream()
                .map(commentMapper::entityToResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<CommentResponse>>builder()
                .message("Fetched comments for post " + postId)
                .result(dtos)
                .build();
    }

    @Override
    public ApiResponse<CommentResponse> findById(String id) {
        CommentEntity entity = commentRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        CommentResponse resp = commentMapper.entityToResponse(entity);
        return ApiResponse.<CommentResponse>builder()
                .message("Fetched comment successfully")
                .result(resp)
                .build();
    }

    @Override
    public ApiResponse<CommentResponse> createComment(CommentCreateRequest request) {
        CommentEntity entity = commentMapper.createRequestToEntity(request);
        UserEntity user = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        CommunityPostEntity post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        entity.setUser(user);
        entity.setPost(post);
        entity = commentRepository.save(entity);
        CommentResponse resp = commentMapper.entityToResponse(entity);
        return ApiResponse.<CommentResponse>builder()
                .message("Comment created successfully")
                .result(resp)
                .build();
    }

    @Override
    public ApiResponse<Void> updateComment(CommentUpdateRequest request) {
        CommentEntity existing = commentRepository.findById(request.getId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        commentMapper.updateEntityFromRequest(request, existing);
        commentRepository.save(existing);
        return ApiResponse.<Void>builder()
                .message("Comment updated successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> deleteComments(CommentDeleteRequest request) {
        request.getIds().forEach(id -> {
            CommentEntity e = commentRepository.findById(id)
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
            commentRepository.delete(e);
        });
        return ApiResponse.<Void>builder()
                .message("Comments deleted successfully")
                .build();
    }
}
