package com.example.demo.user.mapper;


import com.example.demo.user.controllers.dtos.UserResponse;
import com.example.demo.user.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    @Mapping(target = "gardenCount", expression = "java(userEntity.getGardens().size())")
    @Mapping(target = "reminderCount", expression = "java(userEntity.getReminders().size())")
    @Mapping(target = "postCount", expression = "java(userEntity.getCommunityPosts().size())")
    @Mapping(target = "commentCount", expression = "java(userEntity.getComments().size())")
    @Mapping(target = "chatSessionCount", expression = "java(userEntity.getChatbotSessions().size())")
    @Mapping(target = "isDeleted", expression = "java(userEntity.getDeletedAt() != null)")
    UserResponse userEntityToUserResponse(UserEntity userEntity);
}
