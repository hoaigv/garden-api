package com.example.demo.community.like.mapper;

import com.example.demo.community.like.controller.dtos.LikeCreateRequest;
import com.example.demo.community.like.model.LikeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ILikeMapper {

    /**
     * Map create request to new like entity.
     * User and post sẽ được gán ở service.
     */
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    LikeEntity createRequestToEntity(LikeCreateRequest request);
}
