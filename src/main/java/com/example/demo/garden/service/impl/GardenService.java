package com.example.demo.garden.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.common.enums.GardenCondition;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.garden.controller.dtos.*;
import com.example.demo.garden.mapper.IGardenMapper;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.garden.repository.GardenRepository;
import com.example.demo.garden.repository.GardenSpecification;
import com.example.demo.garden.service.IGardenService;
import com.example.demo.gardencell.repository.GardenCellSpecification;
import com.example.demo.gardencell.repository.IGardenCellRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenService implements IGardenService {

    GardenRepository gardenRepository;
    IGardenMapper gardenMapper;
    IUserRepository userRepository;
    IGardenCellRepository cellRepository;

    @Override
    public ApiResponse<List<GardenAdminResponse>> findAll(
            Integer page,
            Integer size,
            String userId,
            String name,
            String condition,
            Integer minRows,
            Integer maxRows,
            Integer minCols,
            Integer maxCols,
            String sortBy,
            String sortDir
    ) {
        // build specification for admin search
        Specification<GardenEntity> spec = GardenSpecification.build(
                null, userId, name, condition, minRows, maxRows, minCols, maxCols
        );


        // build pageable with sorting direction
        Sort.Direction dir = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        Page<GardenEntity> pageResult = gardenRepository.findAll(spec, pageable);

        List<GardenAdminResponse> result = pageResult.getContent().stream()
                .map(
                        gardenEntity -> {
                            var gardenRes = gardenMapper.entityToResponse(gardenEntity);
                            return GardenAdminResponse.builder()
                                    .gardenResponse(gardenRes)
                                    .deletedAt(gardenEntity.getDeletedAt())
                                    .createdAt(gardenEntity.getCreatedAt())
                                    .updatedAt(gardenEntity.getUpdatedAt())
                                    .totalCell(gardenEntity.getColLength() * gardenEntity.getRowLength())
                                    .build();
                        }
                )
                .collect(Collectors.toList());

        return ApiResponse.<List<GardenAdminResponse>>builder()
                .message("Successfully fetched gardens")
                .result(result)
                .total(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<List<GardenResponse>> findAllForCurrentUser() {
        // retrieve current user from security context
        var currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        // build specification for current user without additional filters
        Specification<GardenEntity> spec = GardenSpecification.build(
                null,
                currentUser.getId(),
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<GardenResponse> dtos = gardenRepository.findAll(spec.and(GardenSpecification.isNotDelete())).stream()
                .map(gardenMapper::entityToResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<GardenResponse>>builder()
                .message("Fetched gardens for current user")
                .result(dtos)
                .build();
    }

    @Override
    public ApiResponse<GardenResponse> findById(String id) {
        GardenEntity entity = gardenRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        GardenResponse response = gardenMapper.entityToResponse(entity);
        return ApiResponse.<GardenResponse>builder()
                .message("Successfully fetched garden")
                .result(response)
                .build();
    }

    @Override
    public ApiResponse<GardenResponse> createGarden(CreateGardenRequest request) {
        GardenEntity entity = gardenMapper.createRequestToEntity(request);
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        entity.setUser(user);
        entity.setGardenCondition(GardenCondition.NORMAL);
        entity.setTotalCell(entity.getColLength() * entity.getRowLength());
        entity = gardenRepository.save(entity);
        GardenResponse response = gardenMapper.entityToResponse(entity);
        return ApiResponse.<GardenResponse>builder()
                .message("Garden created successfully")
                .result(response)
                .build();
    }

    @Override
    public ApiResponse<Void> updateGarden(UpdateGardenRequest request) {
        GardenEntity existing = gardenRepository.findById(request.getId().toString())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        gardenMapper.updateEntityFromRequest(request, existing);
        gardenRepository.save(existing);
        return ApiResponse.<Void>builder()
                .message("Garden updated successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> deleteGardens(DeleteGardensRequest request) {
        for (String id : request.getIds()) {
            GardenEntity entity = gardenRepository.findById(id)
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
           if(entity.getDeletedAt() == null) {
               entity.setDeletedAt( LocalDateTime.now() );
               gardenRepository.save(entity);

           }else {
               entity.setDeletedAt(null);
               gardenRepository.save(entity);
           }


        }
        return ApiResponse.<Void>builder()
                .message("Gardens deleted successfully")
                .build();
    }
}
