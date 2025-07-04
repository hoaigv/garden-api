package com.example.demo.gardencell.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.common.enums.GardenCondition;
import com.example.demo.common.enums.HealthStatus;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.garden.repository.GardenRepository;
import com.example.demo.garden.repository.GardenSpecification;
import com.example.demo.gardencell.controller.dtos.*;
import com.example.demo.gardencell.mapper.IGardenCellMapper;
import com.example.demo.gardencell.model.GardenCellEntity;
import com.example.demo.gardencell.repository.IGardenCellRepository;
import com.example.demo.gardencell.repository.GardenCellSpecification;
import com.example.demo.gardencell.service.IGardenCellService;
import com.example.demo.plantInventory.repository.PlantInventoryRepository;
import com.example.demo.plantInventory.repository.PlantInventorySpecification;
import com.example.demo.user.repository.IUserRepository;
import com.example.demo.user.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenCellService implements IGardenCellService {

    IGardenCellRepository cellRepository;
    IGardenCellMapper cellMapper;
    GardenRepository gardenRepository;
    PlantInventoryRepository plantInventoryRepository;
    IUserRepository userRepository;

    @Override
    public ApiResponse<GardenCellsViewResponse> findAll(
            String gardenId,
            String plantInventoryId,
            String status

    ) {
        // parse health status
        HealthStatus hs = null;
        if (status != null && !status.isBlank()) {
            try {
                hs = HealthStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT);
            }
        }

        var garden = gardenRepository.findOne(GardenSpecification.hasId(gardenId))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.GARDEN_NOT_FOUND));

        // build specification
        Specification<GardenCellEntity> spec = GardenCellSpecification.build(
                gardenId,
                plantInventoryId,
                hs
        );

        // fetch *all* matching entities (no pageable, no sort)
        List<GardenCellEntity> entities = cellRepository.findAll(spec);

        // map to view DTO
        var view = cellMapper.toViewResponse(entities);
        view.setGardenId(gardenId);
        view.setColLength(garden.getColLength());
        view.setRowLength(garden.getRowLength());
        return ApiResponse.<GardenCellsViewResponse>builder()
                .message("Successfully fetched all garden cells")
                .result(view)
                .total((long) entities.size())
                .build();
    }

    @Override
    public ApiResponse<GardenCellsAdminResponse> adminFindAll(
            String gardenId,
            String plantInventoryId,
            String status

    ) {
        // parse health status
        HealthStatus hs = null;
        if (status != null && !status.isBlank()) {
            try {
                hs = HealthStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT);
            }
        }

        var garden = gardenRepository.findOne(GardenSpecification.hasId(gardenId))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.GARDEN_NOT_FOUND));

        // build specification
        Specification<GardenCellEntity> spec = GardenCellSpecification.build(
                gardenId,
                plantInventoryId,
                hs
        );

        // fetch *all* matching entities (no pageable, no sort)
        List<GardenCellEntity> entities = cellRepository.findAll(spec);

        // map to view DTO
        var view = cellMapper.toViewResponse(entities);
        view.setGardenId(gardenId);
        view.setColLength(garden.getColLength());
        view.setRowLength(garden.getRowLength());
        Set<String> inventorySet = view.getCells().stream().map(
                cell -> {
                    var inventory = plantInventoryRepository.findOne(PlantInventorySpecification.hasId(cell.getPlantInventoryId()))
                            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT));
                    return inventory.getName();
                }
        ).collect(Collectors.toSet());
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT));
        var resp = GardenCellsAdminResponse.builder()
                .gardenCellsViewResponse(view)
                .createAt(garden.getCreatedAt())
                .gardenCondition(garden.getGardenCondition().toString())
                .inventoryPlantName(inventorySet)
                .userName(user.getName())
                .gardenName(garden.getName())
                .build();
        return ApiResponse.<GardenCellsAdminResponse>builder()
                .result(resp)
                .build();
    }


    @Override
    public ApiResponse<Void> createCellsBatch(String gardenId,
                                              List<CreateGardenCellRequest> requests) {
        // 1. Kiểm tra garden tồn tại
        var garden = gardenRepository.findById(gardenId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.GARDEN_NOT_FOUND));

        // 2. Map về entity, set Garden reference
        List<GardenCellEntity> entities = requests.stream()
                .map(req -> {
                    var inv = plantInventoryRepository.findOne(PlantInventorySpecification.hasId(req.getPlantInventoryId()))
                            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));

                    if (inv.getInventoryQuantity() - req.getQuantity() < 0) {
                        throw new CustomRuntimeException(ErrorCode.INSUFFICIENT_QUANTITY);
                    }
                    inv.setInventoryQuantity(inv.getInventoryQuantity() - req.getQuantity());
                    plantInventoryRepository.save(inv);

                    return GardenCellEntity.builder()
                            .garden(garden)
                            .plantInventory(inv)
                            .rowIndex(req.getRowIndex())
                            .colIndex(req.getColIndex())
                            .quantity(req.getQuantity())
                            .healthStatus(HealthStatus.NORMAL)
                            .build();
                })
                .collect(Collectors.toList());

        // 3. Lưu batch
        cellRepository.saveAll(entities);


        return ApiResponse.<Void>builder()
                .message("Successfully saved all garden cells")
                .build();
    }


    @Override
    public ApiResponse<Void> updateCell(UpdateGardenCellRequest request) {
        GardenCellEntity entity = cellRepository.findById(request.getId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        cellMapper.updateEntityFromRequest(request, entity);
        cellRepository.save(entity);
        return ApiResponse.<Void>builder()
                .message("Garden cell updated successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> updateCellsBatch(UpdateGardenCellsRequest request) {
        var listCellsUpdate = request.getCells();
        if (listCellsUpdate == null || listCellsUpdate.isEmpty()) {
            throw new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        var entities = listCellsUpdate.stream()
                .map(req -> cellRepository.findById(req.getId())
                        .orElseThrow(() -> new CustomRuntimeException(
                                ErrorCode.RESOURCE_NOT_FOUND
                        ))
                )
                .toList();
        for (int i = 0; i < entities.size(); i++) {
            UpdateGardenCellRequest req = listCellsUpdate.get(i);
            GardenCellEntity entity = entities.get(i);
            cellMapper.updateEntityFromRequest(req, entity);
        }
        cellRepository.saveAll(entities);
        var isGardenDisease = cellRepository.findAll(GardenCellSpecification.hasGardenId(entities.get(0).getGarden().getId())
                .and(GardenCellSpecification.hasHealthStatus(HealthStatus.DISEASED))).isEmpty();
        if(!isGardenDisease){
            var gardenCurrent = gardenRepository.findOne(GardenSpecification.hasId(entities.get(0).getGarden().getId()))
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
            gardenCurrent.setGardenCondition(GardenCondition.DISEASED);
            gardenRepository.save(gardenCurrent);
        }

        return ApiResponse.<Void>builder()
                .message("Garden cells updated successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> deleteCells(DeleteGardenCellsRequest request) {
        for (String id : request.getIds()) {
            if (cellRepository.existsById(id)) {
                cellRepository.deleteById(id);
            } else {
                throw new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND);
            }
        }
        return ApiResponse.<Void>builder()
                .message("Garden cells deleted successfully")
                .build();
    }
}
