// src/main/java/com/example/demo/gardenLog/mapper/IGardenLogMapper.java
package com.example.demo.gardenLog.mapper;

import com.example.demo.gardenLog.controller.dto.GardenLogResponse;
import com.example.demo.gardenLog.model.GardenLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IGardenLogMapper {
    IGardenLogMapper INSTANCE = Mappers.getMapper(IGardenLogMapper.class);

    /**
     * Map GardenLogEntity to GardenLogResponse DTO
     * - Use createdAt as timestamp
     */
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "description", source = "entity.description")
    @Mapping(target = "actionType", source = "entity.actionType")
    @Mapping(target = "timestamp", source = "entity.createdAt")
    @Mapping(target = "reminderId", source = "entity.reminder.id")
    @Mapping(target = "gardenId", source = "entity.garden.id")
    GardenLogResponse toResponse(GardenLogEntity entity);

    /**
     * Map list of entities to list of DTOs
     */
    List<GardenLogResponse> toResponseList(List<GardenLogEntity> entities);
}
