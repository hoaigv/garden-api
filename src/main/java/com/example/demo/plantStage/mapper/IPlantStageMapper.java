package com.example.demo.plantStage.mapper;

import com.example.demo.plantInventory.controllers.dtos.PlantInventoryResponse;
import com.example.demo.plantStage.controller.dtos.AdminPlantStageResponse;
import com.example.demo.plantStage.controller.dtos.CreatePlantStageRequest;
import com.example.demo.plantStage.controller.dtos.PlantStageResponse;
import com.example.demo.plantStage.controller.dtos.UpdatePlantStageRequest;
import com.example.demo.plantStage.model.PlantStageEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IPlantStageMapper {

    /**
     * Map CreatePlantStageRequest to PlantStageEntity.
     */


    PlantStageEntity createRequestToEntity(CreatePlantStageRequest request);

    /**
     * Update an existing PlantStageEntity from UpdatePlantStageRequest.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    PlantStageEntity updateEntityFromRequest(UpdatePlantStageRequest request, @MappingTarget PlantStageEntity entity);

    /**
     * Map PlantStageEntity to PlantStageResponse.
     */


    PlantStageResponse entityToResponse(PlantStageEntity entity);

    /**
     * Map list of entities to list of responses.
     */


    List<PlantStageResponse> entitiesToResponses(List<PlantStageEntity> entities);

    List<AdminPlantStageResponse> entitiesToResponsesAdmin(List<PlantStageEntity> entities);


}
