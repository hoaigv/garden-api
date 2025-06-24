package com.example.demo.garden.mapper;

import com.example.demo.garden.controller.dtos.CreateGardenRequest;
import com.example.demo.garden.controller.dtos.GardenResponse;
import com.example.demo.garden.controller.dtos.UpdateGardenRequest;
import com.example.demo.garden.model.GardenEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IGardenMapper {

    /**
     * Map CreateGardenRequest to GardenEntity (for new gardens).
     * The id and relations will be set by the service layer or JPA.
     */


    @Mapping(target = "cells", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "reminders", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "aiSuggestions", ignore = true)
    GardenEntity createRequestToEntity(CreateGardenRequest request);

//    /**
//     * Map UpdateGardenRequest to GardenEntity for full replace/update.
//     * Typically used to create a new entity instance; alternative is to use @MappingTarget.
//     */
//    @InheritConfiguration(name = "createRequestToEntity")
//    @Mapping(target = "user.id", ignore = true)
//    GardenEntity updateRequestToEntity(UpdateGardenRequest request);

    /**
     * Update an existing GardenEntity in-place from UpdateGardenRequest.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cells", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "reminders", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "aiSuggestions", ignore = true)
    void updateEntityFromRequest(UpdateGardenRequest request, @MappingTarget GardenEntity entity);

    /**
     * Map single GardenEntity to GardenResponse (for GET operations).
     * Count relations for summary.
     */
    @Mapping(target = "userId", expression = "java(entity.getUser().getId())")
    GardenResponse entityToResponse(GardenEntity entity);

    /**
     * Map list of entities to list of responses.
     */
    List<GardenResponse> entitiesToResponses(List<GardenEntity> entities);
}
