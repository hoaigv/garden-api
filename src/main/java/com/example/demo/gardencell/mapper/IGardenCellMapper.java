package com.example.demo.gardencell.mapper;

import com.example.demo.gardencell.controller.dtos.CreateGardenCellRequest;
import com.example.demo.gardencell.controller.dtos.UpdateGardenCellRequest;
import com.example.demo.gardencell.controller.dtos.GardenCellResponse;
import com.example.demo.gardencell.controller.dtos.GardenCellsViewResponse;
import com.example.demo.gardencell.model.GardenCellEntity;
import com.example.demo.plantStage.mapper.IPlantStageMapper;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface IGardenCellMapper {


    /**
     * Map CreateGardenCellRequest to GardenCellEntity.
     */


    GardenCellEntity createRequestToEntity(CreateGardenCellRequest request);

    /**
     * Update an existing GardenCellEntity from UpdateGardenCellRequest.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "garden", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    void updateEntityFromRequest(UpdateGardenCellRequest request, @MappingTarget GardenCellEntity entity);

    /**
     * Map GardenCellEntity to GardenCellResponse.
     */

    @Mapping(target = "plantVariety", ignore = true)

    GardenCellResponse entityToSummary(GardenCellEntity entity);

    /**
     * Map list of GardenCellEntity to GardenCellsViewResponse wrapper.
     */
    default GardenCellsViewResponse toViewResponse(List<GardenCellEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return GardenCellsViewResponse.builder()
                    .build();
        }
        List<GardenCellResponse> cells = new ArrayList<>();
        for (GardenCellEntity entity : entities) {
            GardenCellResponse gardenCellResponse = entityToSummary(entity);
            cells.add(gardenCellResponse);
        }
        return GardenCellsViewResponse.builder()
                .cells(cells)
                .build();
    }

    /**
     * Map list of GardenCellEntity to list of GardenCellResponse.
     */
    @Mapping(target = "cells" , ignore = true)
    List<GardenCellResponse> entitiesToSummaries(List<GardenCellEntity> entities);
}
