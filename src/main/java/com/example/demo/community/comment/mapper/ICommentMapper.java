package com.example.demo.community.comment.mapper;

import com.example.demo.community.comment.controller.dtos.*;
import com.example.demo.community.comment.model.CommentEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICommentMapper {

    /**
     * Map create request to new entity.
     * Relations (user, post) set in service layer.
     */
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "user", ignore = true)
    CommentEntity createRequestToEntity(CommentCreateRequest request);

    /**
     * In-place update of existing entity from update request.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromRequest(CommentUpdateRequest request, @MappingTarget CommentEntity entity);

    /**
     * Map entity to response DTO.
     */
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userLink" , source = "user.avatarLink")
    CommentResponse entityToResponse(CommentEntity entity);

    /**
     * Map list of entities to list of responses.
     */
    List<CommentResponse> entitiesToResponses(List<CommentEntity> entities);
}
