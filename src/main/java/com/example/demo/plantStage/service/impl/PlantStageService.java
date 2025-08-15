package com.example.demo.plantStage.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.plantStage.controller.dtos.CreatePlantStageRequest;
import com.example.demo.plantStage.controller.dtos.DeleteStagesRequest;
import com.example.demo.plantStage.controller.dtos.UpdatePlantStageRequest;
import com.example.demo.plantStage.mapper.IPlantStageMapper;
import com.example.demo.plantStage.model.PlantStageEntity;
import com.example.demo.plantStage.repository.IPlantStageRepository;
import com.example.demo.plantStage.repository.PlantStageSpecification;
import com.example.demo.plantStage.service.IPlantStageService;
import com.example.demo.plantVariety.repository.IPlantVarietyRepository;
import com.example.demo.plantVariety.repository.PlantVarietySpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlantStageService implements IPlantStageService {

    IPlantVarietyRepository plantVarietyRepository;

    IPlantStageRepository plantStageRepository;

    IPlantStageMapper plantStageMapper;

    @Override
    public ApiResponse<Void> CreateStage(CreatePlantStageRequest request) {
        var variety = plantVarietyRepository.findOne(PlantVarietySpecification.hasId(request.getVarietyId()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));

        for (var stage : variety.getStages()) {
            if (Objects.equals(stage.getStageOrder(), request.getStageOrder())) {
                throw new CustomRuntimeException(ErrorCode.STAGE_ORDER_ALREADY_EXISTS);
            }
        }

        var stage = plantStageMapper.createRequestToEntity(request);
        stage.setPlantVariety(variety);
        plantStageRepository.save(stage);
        return ApiResponse.<Void>builder()
                .message("Create stages successfully !")
                .build();
    }

    @Override
    public ApiResponse<Void> updateStage(UpdatePlantStageRequest request) {
        var oldStage = plantStageRepository.findOne(PlantStageSpecification.hasId(request.getId()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));

        var variety = plantVarietyRepository.findOne(PlantVarietySpecification.hasId(oldStage.getPlantVariety().getId()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));

       if(!Objects.equals(oldStage.getStageOrder(), request.getStageOrder())) {
           for (var stage : variety.getStages()) {
               if (Objects.equals(stage.getStageOrder(), request.getStageOrder())) {
                   throw new CustomRuntimeException(ErrorCode.STAGE_ORDER_ALREADY_EXISTS);
               }
           }
       }
        var stage = plantStageMapper.updateEntityFromRequest(request, oldStage);
        plantStageRepository.save(stage);
        return null;
    }

    @Override
    public ApiResponse<Void> DeleteStages(DeleteStagesRequest request) {
        for (var id : request.getIds()) {
            var stage = plantStageRepository.findOne(PlantStageSpecification.hasId(id))
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
            if (stage.getDeletedAt() == null) {
                stage.setDeletedAt(LocalDateTime.now());
            } else {
                if (plantStageRepository.exists(PlantStageSpecification.hasPlantVarietyId(request.getVarietyId())
                        .and(PlantStageSpecification.hasStageOrder(stage.getStageOrder()))
                        .and(PlantStageSpecification.isNotDelete()))) {
                    throw new CustomRuntimeException(ErrorCode.STAGE_ORDER_ALREADY_EXISTS);
                }
                stage.setDeletedAt(null);
            }

            plantStageRepository.save(stage);
        }
        return ApiResponse.<Void>builder()
                .message("Delete stages successfully !")
                .build();
    }
}
