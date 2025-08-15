package com.example.demo.plantVariety.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.enums.PlantTypeEnum;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.garden.controller.dtos.GardenAdminResponse;
import com.example.demo.garden.repository.GardenRepository;
import com.example.demo.garden.repository.GardenSpecification;
import com.example.demo.gardencell.repository.GardenCellSpecification;
import com.example.demo.gardencell.repository.IGardenCellRepository;
import com.example.demo.plantStage.controller.dtos.AdminPlantStageResponse;
import com.example.demo.plantStage.mapper.IPlantStageMapper;
import com.example.demo.plantStage.repository.IPlantStageRepository;
import com.example.demo.plantStage.repository.PlantStageSpecification;
import com.example.demo.plantVariety.controller.dtos.*;
import com.example.demo.plantVariety.mapper.IPlantVarietyMapper;
import com.example.demo.plantVariety.model.PlantVarietyEntity;
import com.example.demo.plantVariety.repository.IPlantVarietyRepository;
import com.example.demo.plantVariety.repository.PlantVarietySpecification;
import com.example.demo.plantVariety.service.IPlantVarietyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlantVarietyService implements IPlantVarietyService {

    IPlantVarietyMapper plantVarietyMapper;
    IPlantVarietyRepository plantVarietyRepository;
    IPlantStageRepository plantStageRepository;
    IPlantStageMapper plantStageMapper;
    GardenRepository gardenRepository;
    IGardenCellRepository cellRepository;


    @Override
    public ApiResponse<Void> CreateVariety(CreatePlantVarietyRequest request) {
        var entity = plantVarietyMapper.createRequestToEntity(request);
        var plantType = PlantTypeEnum.valueOf(request.getPlantType());
        entity.setPlantType(plantType);
        plantVarietyRepository.save(entity);
        return ApiResponse.<Void>builder()
                .message("Add variety successfully !")
                .build();
    }

    @Override
    public ApiResponse<List<AdminVarietyResponse>> findAll(Integer page, Integer size, String sortBy, String sortDir) {
        Sort.Direction dir = "asc".equals(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        Page<PlantVarietyEntity> pageResult = plantVarietyRepository.findAll(pageable);
        var result = pageResult.getContent().stream().map(
                variety -> {
                    var cells = variety.getGardenCells().size();
                    var resp = plantVarietyMapper.entityToResponseAdmin(variety);
                    resp.setNumCellsWithVariety(cells);
                    return resp;
                }
        ).toList();

        return ApiResponse.<List<AdminVarietyResponse>>builder()
                .message("Find variety successfully !")
                .result(result)
                .total(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<Void> UpdateVariety(UpdatePlantVarietyRequest request, String varietyId) {
        var oldEntity = plantVarietyRepository.findById(varietyId)
                .orElseThrow(
                        () -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND)
                );
        var entity = plantVarietyMapper.updateEntityFromRequest(request, oldEntity);
        plantVarietyRepository.save(entity);
        return ApiResponse.<Void>builder()
                .message("Update variety successfully !")
                .build();
    }

    @Override
    public ApiResponse<List<PlantVarietyResponse>> getAllVarieties(String plantType) {


        var filter = plantType.equals("ALL") ?
                PlantVarietySpecification.isNotDelete() :
                PlantVarietySpecification.hasPlantType(
                        PlantTypeEnum.valueOf(plantType)
                );

        var varieties = plantVarietyRepository.findAll(filter);
        var result = varieties.stream().filter(
                variety -> !variety.getStages().isEmpty()
        ).toList();
        var resp = result.stream().map(
                plantVarietyMapper::entityToResponse
        ).toList();

        return ApiResponse.<List<PlantVarietyResponse>>builder()
                .result(resp)
                .build();
    }

    @Override
    public ApiResponse<VarietyDetailResponse> getDetailVarietyDetail(String varietyId) {
        var variety = plantVarietyRepository.findOne(PlantVarietySpecification.hasId(varietyId))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        var stages = plantStageRepository.findAll(PlantStageSpecification.hasPlantVarietyId(varietyId)
                .and(PlantStageSpecification.isNotDelete()));

        var res = plantVarietyMapper.entityToResponseDetail(variety);
        res.setStages(plantStageMapper.entitiesToResponses(stages));

        return ApiResponse.<VarietyDetailResponse>builder()
                .result(res)
                .build();
    }

    @Override
    public ApiResponse<AdminVarietyDetailResponse> getAdminVarietyDetail(String varietyId) {
        var variety = plantVarietyRepository.findOne(PlantVarietySpecification.hasId(varietyId))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        var stages = plantStageRepository.findAll(PlantStageSpecification.hasPlantVarietyId(varietyId));
        var stagesMapped = stages.stream().map(
                stage -> {
                    var stageMapped = plantStageMapper.entityToResponse(stage);
                    return AdminPlantStageResponse.builder()
                            .createdAt(stage.getCreatedAt())
                            .updatedAt(stage.getUpdatedAt())
                            .deletedAt(stage.getDeletedAt())
                            .plantStageResponse(stageMapped)
                            .build();
                }
        ).toList();
        var res = plantVarietyMapper.entityToResponseDetailAdmin(variety);
        res.setStages(stagesMapped);
        return ApiResponse.<AdminVarietyDetailResponse>builder()
                .result(res)
                .build();
    }

    @Override
    public ApiResponse<Void> DeleteVariety(List<String> ids) {

        for (String id : ids) {
            var variety = plantVarietyRepository.findOne(PlantVarietySpecification.hasId(id))
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
            if (variety.getDeletedAt() == null) {
                variety.setDeletedAt(LocalDateTime.now());
            } else {
                variety.setDeletedAt(null);
            }
            plantVarietyRepository.save(variety);
        }

        return ApiResponse.<Void>builder()
                .message("Delete variety successfully !")
                .build();
    }
}
