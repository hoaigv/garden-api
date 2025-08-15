package com.example.demo.plantStage.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.plantStage.controller.dtos.CreatePlantStageRequest;
import com.example.demo.plantStage.controller.dtos.DeleteStagesRequest;
import com.example.demo.plantStage.controller.dtos.UpdatePlantStageRequest;
import com.example.demo.plantVariety.controller.dtos.CreatePlantVarietyRequest;

import java.util.List;

public interface IPlantStageService {
    ApiResponse<Void> CreateStage(CreatePlantStageRequest request);

    ApiResponse<Void> updateStage(UpdatePlantStageRequest request);

    ApiResponse<Void> DeleteStages(DeleteStagesRequest request);

}
