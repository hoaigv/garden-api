package com.example.demo.plantInventory.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.common.enums.PlantTypeEnum;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.plantInventory.controllers.dtos.*;
import com.example.demo.plantInventory.mapper.IPlantInventoryMapper;
import com.example.demo.plantInventory.model.PlantInventoryEntity;
import com.example.demo.plantInventory.repository.PlantInventoryRepository;
import com.example.demo.plantInventory.repository.PlantInventorySpecification;
import com.example.demo.plantInventory.service.IPlantInventoryService;
import com.example.demo.plantVariety.mapper.IPlantVarietyMapper;
import com.example.demo.plantVariety.repository.IPlantVarietyRepository;
import com.example.demo.plantVariety.repository.PlantVarietySpecification;
import com.example.demo.user.repository.IUserRepository;
import com.example.demo.user.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlantInventoryService implements IPlantInventoryService {

    PlantInventoryRepository inventoryRepository;
    IPlantInventoryMapper inventoryMapper;
    IUserRepository userRepository;
    IPlantVarietyRepository plantVarietyRepository;
    IPlantVarietyMapper plantVarietyMapper;

    @Override
    public ApiResponse<List<PlantInventoryAdminResponse>> findAll(
            Integer page,
            Integer size,
            String userId,
            String sortBy,
            String sortDir
    ) {
        // build specification
        PlantTypeEnum typeEnum = null;

        Specification<PlantInventoryEntity> spec = PlantInventorySpecification.build(
                null,
                userId
        );


        // pageable
        Sort.Direction dir = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        Page<PlantInventoryEntity> pageResult = inventoryRepository.findAll(spec, pageable);

        var result = pageResult.getContent().stream()
                .map(entity -> {
                    var inven = inventoryMapper.entityToResponse(entity);
                    return PlantInventoryAdminResponse.builder()
                            .plantInventoryResponse(inven)
                            .createdAt(entity.getCreatedAt())
                            .updatedAt(entity.getUpdatedAt())
                            .deletedAt(entity.getDeletedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return ApiResponse.<List<PlantInventoryAdminResponse>>builder()
                .message("Successfully fetched plant inventories")
                .result(result)
                .total(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<List<PlantInventoryResponse>> findAllForCurrentUser() {
        // Lấy user hiện tại
        var currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        // Query tất cả plant inventories của user này
        List<PlantInventoryResponse> dtos = new ArrayList<>();
        for (PlantInventoryEntity plantInventoryEntity : inventoryRepository
                .findAll(PlantInventorySpecification.hasUserId(currentUser.getId()))) {
            var plantInventoryResponse = inventoryMapper.entityToResponse(plantInventoryEntity);
            var variety = plantVarietyRepository.findOne(PlantVarietySpecification.hasId(plantInventoryEntity.getPlantVariety().getId()))
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
            plantInventoryResponse.setPlantVariety(plantVarietyMapper.entityToResponse(variety));
            dtos.add(plantInventoryResponse);
        }

        return ApiResponse.<List<PlantInventoryResponse>>builder()
                .result(dtos)
                .build();
    }


    @Override
    public ApiResponse<Void> createPlantInventory(CreatePlantInventoryRequest request) {
        // verify user exists
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        var variety = plantVarietyRepository.findOne(PlantVarietySpecification.hasId(request.getVarietyId()))
                .orElseThrow();


        var entity = PlantInventoryEntity.builder()
                .numberOfVariety(request.getNumberOfVariety())
                .plantVariety(variety)
                .user(user)
                .build();
        entity.setUser(user);
        inventoryRepository.save(entity);
        return ApiResponse.<Void>builder()
                .message("Plant inventory created successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> updatePlantInventory(UpdatePlantInventoryRequest request) {
        PlantInventoryEntity existing = inventoryRepository.findById(request.getId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        inventoryMapper.updateEntityFromRequest(request, existing);
        inventoryRepository.save(existing);
        return ApiResponse.<Void>builder()
                .message("Plant inventory updated successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> deletePlantInventories(DeletePlantInventoriesRequest request) {
        for (String id : request.getIds()) {
            if (inventoryRepository.existsById(id)) {
                inventoryRepository.deleteById(id);
            } else {
                throw new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND);
            }
        }
        return ApiResponse.<Void>builder()
                .message("Plant inventories deleted successfully")
                .build();
    }
}
