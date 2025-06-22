package com.example.demo.plantInventory.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.common.enums.PlantTypeEnum;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.plantInventory.controllers.dtos.CreatePlantInventoryRequest;
import com.example.demo.plantInventory.controllers.dtos.DeletePlantInventoriesRequest;
import com.example.demo.plantInventory.controllers.dtos.PlantInventoryResponse;
import com.example.demo.plantInventory.controllers.dtos.UpdatePlantInventoryRequest;
import com.example.demo.plantInventory.mapper.IPlantInventoryMapper;
import com.example.demo.plantInventory.model.PlantInventoryEntity;
import com.example.demo.plantInventory.repository.PlantInventoryRepository;
import com.example.demo.plantInventory.repository.PlantInventorySpecification;
import com.example.demo.plantInventory.service.IPlantInventoryService;
import com.example.demo.user.repository.UserRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlantInventoryService implements IPlantInventoryService {

    PlantInventoryRepository inventoryRepository;
    IPlantInventoryMapper inventoryMapper;
    UserRepository userRepository;

    @Override
    public ApiResponse<List<PlantInventoryResponse>> findAll(
            Integer page,
            Integer size,
            String userId,
            String plantType,
            String sortBy,
            String sortDir
    ) {
        // build specification
        PlantTypeEnum typeEnum = null;
        if (plantType != null && !plantType.isBlank()) {
            try {
                typeEnum = PlantTypeEnum.valueOf(plantType);
            } catch (IllegalArgumentException e) {
                throw new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT);
            }
        }

        Specification<PlantInventoryEntity> spec = PlantInventorySpecification.build(
                null,
                userId,
                typeEnum,
                null,
                null,
                null,
                null,
                plantType
        );


        // pageable
        Sort.Direction dir = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        Page<PlantInventoryEntity> pageResult = inventoryRepository.findAll(spec, pageable);

        List<PlantInventoryResponse> result = pageResult.getContent().stream()
                .map(inventoryMapper::entityToResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<PlantInventoryResponse>>builder()
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
        List<PlantInventoryResponse> dtos = inventoryRepository
                .findAll(PlantInventorySpecification.hasUserId(currentUser.getId()))
                .stream()
                .map(inventoryMapper::entityToResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<PlantInventoryResponse>>builder()
                .message("Fetched plant inventories for current user")
                .result(dtos)
                .build();
    }


    @Override
    public ApiResponse<Void> createPlantInventory(CreatePlantInventoryRequest request) {
        // verify user exists
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        PlantInventoryEntity entity = inventoryMapper.createRequestToEntity(request);
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
