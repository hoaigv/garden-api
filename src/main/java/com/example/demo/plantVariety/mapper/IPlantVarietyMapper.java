package com.example.demo.plantVariety.mapper;

import com.example.demo.plantVariety.controller.dtos.*;
import com.example.demo.plantVariety.model.PlantVarietyEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface IPlantVarietyMapper {

    /**
     * Map CreatePlantVarietyRequest to PlantVarietyEntity.
     */

    @Mapping(target = "plantType", ignore = true)
    PlantVarietyEntity createRequestToEntity(CreatePlantVarietyRequest request);

    /**
     * Update an existing PlantVarietyEntity from UpdatePlantVarietyRequest.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stages", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    PlantVarietyEntity updateEntityFromRequest(UpdatePlantVarietyRequest request, @MappingTarget PlantVarietyEntity entity);

    /**
     * Map PlantVarietyEntity to PlantVarietyResponse.
     */
    @Mapping(target = "stages", ignore = true)
    VarietyDetailResponse entityToResponseDetail(PlantVarietyEntity entity);

    /**
     * Map PlantVarietyEntity to PlantVarietyResponse.
     */
    @Mapping(target = "stages", ignore = true)
    AdminVarietyDetailResponse entityToResponseDetailAdmin(PlantVarietyEntity entity);


    @Mapping(target = "numCellsWithVariety", ignore = true)
    AdminVarietyResponse entityToResponseAdmin(PlantVarietyEntity entity);

    @Mapping(target = "id", expression = "java(String.valueOf(entity.getId()))")
    PlantVarietyResponse entityToResponse(PlantVarietyEntity entity);


}
