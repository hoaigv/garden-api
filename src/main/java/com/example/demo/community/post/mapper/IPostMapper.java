package com.example.demo.community.post.mapper;

import com.example.demo.community.post.controller.dtos.CommunityPostCreateRequest;
import com.example.demo.community.post.controller.dtos.CommunityPostUpdateRequest;
import com.example.demo.community.post.controller.dtos.CommunityPostResponse;
import com.example.demo.community.post.model.CommunityPostEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IPostMapper {

    /**
     * Map create request to new entity.
     * Relations and audit fields set in service layer.
     */

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "user", ignore = true)
    CommunityPostEntity createRequestToEntity(CommunityPostCreateRequest request);

    /**
     * In-place update of existing entity from update request.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "comments", ignore = true)
    void updateEntityFromRequest(CommunityPostUpdateRequest request, @MappingTarget CommunityPostEntity entity);

    /**
     * Map entity to response DTO, including count of comments.
     */

    CommunityPostResponse entityToResponse(CommunityPostEntity entity);

    /**
     * Map list of entities to list of responses.
     */
    List<CommunityPostResponse> entitiesToResponses(List<CommunityPostEntity> entities);
}
