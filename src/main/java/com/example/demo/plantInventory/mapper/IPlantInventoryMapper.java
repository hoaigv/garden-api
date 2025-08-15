package com.example.demo.plantInventory.mapper;

import com.example.demo.plantInventory.controllers.dtos.CreatePlantInventoryRequest;
import com.example.demo.plantInventory.controllers.dtos.UpdatePlantInventoryRequest;
import com.example.demo.plantInventory.controllers.dtos.PlantInventoryResponse;
import com.example.demo.plantInventory.model.PlantInventoryEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IPlantInventoryMapper {

    /**
     * Map CreatePlantInventoryRequest to PlantInventoryEntity.
     */


    PlantInventoryEntity createRequestToEntity(CreatePlantInventoryRequest request);

    /**
     * Update an existing PlantInventoryEntity from UpdatePlantInventoryRequest.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    PlantInventoryEntity updateEntityFromRequest(UpdatePlantInventoryRequest request, @MappingTarget PlantInventoryEntity entity);

    /**
     * Map PlantInventoryEntity to PlantInventoryResponse.
     */
    @Mapping(target = "id", expression = "java(String.valueOf(entity.getId()))")
    @Mapping(target = "plantVariety", ignore = true)
    PlantInventoryResponse entityToResponse(PlantInventoryEntity entity);

    /**
     * Map list of entities to list of responses.
     */
    List<PlantInventoryResponse> entitiesToResponses(List<PlantInventoryEntity> entities);

    /**
     * Helper to convert String id to Long for user.id mapping.
     */
    @Named("stringToLong")
    default Long stringToLong(String id) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
