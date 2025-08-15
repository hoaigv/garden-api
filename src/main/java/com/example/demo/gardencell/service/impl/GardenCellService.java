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
import com.example.demo.plantStage.mapper.IPlantStageMapper;
import com.example.demo.plantStage.repository.IPlantStageRepository;
import com.example.demo.plantStage.repository.PlantStageSpecification;
import com.example.demo.plantVariety.mapper.IPlantVarietyMapper;
import com.example.demo.plantVariety.repository.IPlantVarietyRepository;
import com.example.demo.plantVariety.repository.PlantVarietySpecification;
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
    IPlantVarietyRepository plantVarietyRepository;
    IPlantStageRepository plantStageRepository;
    IPlantStageMapper plantStageMapper;
    IPlantVarietyMapper plantVarietyMapper;

    @Override
    public ApiResponse<GardenCellsViewResponse> findAll(
            String gardenId
    ) {
        var garden = gardenRepository.findOne(GardenSpecification.hasId(gardenId))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.GARDEN_NOT_FOUND));
        // build specification
        Specification<GardenCellEntity> spec = GardenCellSpecification.build(
                gardenId, null, null
        );
        // fetch *all* matching entities (no pageable, no sort)
        List<GardenCellEntity> entities = cellRepository.findAll(spec);
        // map to view DTO
        var gardenCells = entities.stream().map(
                entity -> {
                    var variety = plantVarietyMapper.entityToResponse(entity.getPlantVariety());
                    var stage = plantStageMapper.entityToResponse(entity.getPlantStage());
                    var cell = cellMapper.entityToSummary(entity);
                    cell.setPlantVariety(variety);
                    cell.setStageLink(stage.getIconLink());
                    cell.setStageGrow(stage.getName());
                    cell.setCreatedAt(entity.getCreatedAt());
                    cell.setImgCellCurrent(entity.getImgCellCurrent());
                    return cell;
                }
        ).toList();


        var view = GardenCellsViewResponse.builder()
                .cells(gardenCells)
                .rowLength(garden.getRowLength())
                .colLength(garden.getColLength())
                .gardenId(gardenId)
                .build();


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

        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT));
        var resp = GardenCellsAdminResponse.builder()
                .gardenCellsViewResponse(view)
                .createAt(garden.getCreatedAt())
                .gardenCondition(garden.getGardenCondition().toString())
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
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT));

        // 2. Map về entity, set Garden reference
        List<GardenCellEntity> entities = requests.stream()
                .map(req -> {
                    var inv = plantInventoryRepository.findOne(PlantInventorySpecification.hasUserId(user.getId())
                                    .and(PlantInventorySpecification.hasVarietyId(req.getVarietyId())))
                            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT));


                    if (inv.getNumberOfVariety() - req.getQuantity() < 0) {
                        throw new CustomRuntimeException(ErrorCode.INSUFFICIENT_QUANTITY);
                    }
                    inv.setNumberOfVariety(inv.getNumberOfVariety() - req.getQuantity());
                    plantInventoryRepository.save(inv);

                    var variety = plantVarietyRepository.findOne(PlantVarietySpecification.hasId(req.getVarietyId()))
                            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));

                    var stage = plantStageRepository.findOne(PlantStageSpecification.hasPlantVarietyId(req.getVarietyId())
                                    .and(PlantStageSpecification.hasStageOrder(1)))
                            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));

                    return GardenCellEntity.builder()
                            .garden(garden)
                            .plantStage(stage)
                            .plantVariety(variety)
                            .rowIndex(req.getRowIndex())
                            .colIndex(req.getColIndex())
                            .quantity(req.getQuantity())
                            .healthStatus(HealthStatus.NORMAL)
                            .imgCellCurrent(stage.getIconLink())
                            .build();
                })
                .collect(Collectors.toList());

        // 3. Lưu batch
        cellRepository.saveAll(entities);
        garden.setTotalCell(garden.getTotalCell() + requests.size());
        return ApiResponse.<Void>builder()
                .message("Successfully saved all garden cells")
                .build();
    }


    @Override
    public ApiResponse<Void> updateCell(UpdateGardenCellRequest request) {
        GardenCellEntity entity = cellRepository.findById(request.getId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        cellMapper.updateEntityFromRequest(request, entity);
        if (entity.getHealthStatus().equals(HealthStatus.NORMAL)) {
            entity.setDiseaseName(null);
        }
        cellRepository.save(entity);
        if (cellRepository.equals(GardenCellSpecification.hasGardenId(entity.getGarden().getId()).and(
                GardenCellSpecification.hasHealthStatus(HealthStatus.DISEASED)
        ))) {
            var garden = gardenRepository.findOne(GardenSpecification.hasId(entity.getGarden().getId()))
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
            garden.setGardenCondition(GardenCondition.DISEASED);
        } else {
            var garden = gardenRepository.findOne(GardenSpecification.hasId(entity.getGarden().getId()))
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
            garden.setGardenCondition(GardenCondition.NORMAL)
            ;
        }
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
        for (var cell : listCellsUpdate) {

            var cellEntity = cellRepository.findById(cell.getId())
                    .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
            // Update Next Stage
            if (cell.getNextStage()) {
                var currentStageOrder = cellEntity.getPlantStage();
                var nextStage = plantStageRepository.findOne(
                        PlantStageSpecification.hasStageOrder(currentStageOrder.getStageOrder() + 1)
                                .and(PlantStageSpecification.hasPlantVarietyId(currentStageOrder.getPlantVariety().getId()))
                ).orElseThrow(
                        () -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND)
                );
                cellEntity.setPlantStage(nextStage);
                cellEntity.setImgCellCurrent(nextStage.getIconLink());
            }
            if (cell.getImgCellCurrent() != null && cell.getImgCellCurrent().trim().length() > 0) {
                cellEntity.setImgCellCurrent(cell.getImgCellCurrent());
            }
            // Update Cell Position
            if ((cell.getColIndex() != null) && (cell.getRowIndex() != null)) {
                var garden = gardenRepository.findOne(GardenSpecification.hasId(request.getGardenId()))
                        .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
                if ((garden.getColLength() < cell.getColIndex()) && garden.getRowLength() < cell.getRowIndex())
                    throw new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT);
                if (cellRepository.exists(GardenCellSpecification.hasColIndex(cell.getColIndex()).and(
                        GardenCellSpecification.hasRowIndex(cell.getRowIndex()).and(GardenCellSpecification.hasGardenId(request.getGardenId()))
                )))
                    throw new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT);
                cellEntity.setColIndex(cell.getColIndex());
                cellEntity.setRowIndex(cell.getRowIndex());
            }
            // Update Health Status
            if (cell.getHealthStatus() != null) {
                var healthStatus = HealthStatus.valueOf(cell.getHealthStatus());
                if (healthStatus == HealthStatus.DISEASED) {
                    if (cell.getDiseaseName() == null) {
                        throw new CustomRuntimeException(ErrorCode.INVALID_ARGUMENT);
                    }
                    cellEntity.setDiseaseName(cell.getDiseaseName());
                }
                if (healthStatus == HealthStatus.NORMAL) {
                    cellEntity.setDiseaseName(null);
                }
                cellEntity.setHealthStatus(healthStatus);

            }
            cellRepository.save(cellEntity);
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
