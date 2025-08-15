package com.example.demo.plantVariety.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.garden.controller.dtos.GardenAdminResponse;
import com.example.demo.plantVariety.controller.dtos.*;

import java.util.List;

public interface IPlantVarietyService {
    ApiResponse<Void> CreateVariety(CreatePlantVarietyRequest request);

    ApiResponse<Void> UpdateVariety(UpdatePlantVarietyRequest request, String varietyId);

    ApiResponse<List<PlantVarietyResponse>> getAllVarieties(
            String plantType
    );

    ApiResponse<List<AdminVarietyResponse>> findAll(
            Integer page,
            Integer size,
            String sortBy,
            String sortDir
    );

    ApiResponse<VarietyDetailResponse> getDetailVarietyDetail(String varietyId);

    ApiResponse<AdminVarietyDetailResponse> getAdminVarietyDetail(String varietyId);



    ApiResponse<Void> DeleteVariety(List<String> ids);
}
