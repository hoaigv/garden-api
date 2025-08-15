package com.example.demo.gardenHealth.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.enums.HealthStatus;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.garden.repository.GardenRepository;
import com.example.demo.garden.repository.GardenSpecification;
import com.example.demo.gardenHealth.controller.dto.GardenHealthResponse;
import com.example.demo.gardenHealth.model.GardenHealthEntity;
import com.example.demo.gardenHealth.repository.GardenHealthSpecification;
import com.example.demo.gardenHealth.repository.IGardenHealthRepository;
import com.example.demo.gardencell.model.GardenCellEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenHealthService {
    IGardenHealthRepository gardenHealthRepository;
    GardenRepository gardenRepository;

    public ApiResponse<List<GardenHealthResponse>> getGardenHeath(String gardenId) {
        var garden = gardenRepository.findOne(GardenSpecification
                        .hasId(gardenId))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.GARDEN_NOT_FOUND));
        var healths = gardenHealthRepository.findAll(GardenHealthSpecification.hasGardenId(gardenId));
        var resp = healths.stream()
                .map(
                        heath -> {
                            return GardenHealthResponse.builder()
                                    .gardenId(gardenId)
                                    .id(heath.getId())
                                    .healthStatus(heath.getHealthStatus().name())
                                    .diseaseName(heath.getDiseaseName() != null ? heath.getDiseaseName() : "")
                                    .normalCell(heath.getNormalCell())
                                    .deadCell(heath.getDeadCell())
                                    .diseaseCell(heath.getDiseaseCell())
                                    .createdAt(heath.getCreatedAt())
                                    .build();
                        }
                )
                .toList();
        return ApiResponse.<List<GardenHealthResponse>>builder()
                .result(resp)
                .build();


    }

    public void writeHealth() {

        List<GardenEntity> gardens = gardenRepository.findAll(GardenSpecification.isNotDelete());

        for (GardenEntity garden : gardens) {
            int normalCount = 0;
            int deadCount = 0;
            int diseaseCount = 0;
            Set<String> diseaseNames = new LinkedHashSet<>(); // giữ thứ tự và loại trùng

            // đảm bảo cells đã được load trong transaction (LAZY safe)
            for (GardenCellEntity cell : garden.getCells()) {
                HealthStatus hs = cell.getHealthStatus();
                if (hs == null) {
                    // fallback: nếu có diseaseName thì coi là disease, ngược lại normal
                    if (cell.getDiseaseName() != null && !cell.getDiseaseName().isBlank()) {
                        diseaseCount++;
                        diseaseNames.add(cell.getDiseaseName().trim());
                    } else {
                        normalCount++;
                    }
                } else {
                    switch (hs) {
                        case NORMAL -> normalCount++;
                        case DEAD -> deadCount++;
                        case DISEASED -> {
                            diseaseCount++;
                            if (cell.getDiseaseName() != null && !cell.getDiseaseName().isBlank()) {
                                diseaseNames.add(cell.getDiseaseName().trim());
                            }
                        }
                        default -> {
                            // Nếu enum có trạng thái khác, xử lý tùy ý — tạm coi là normal
                            normalCount++;
                        }
                    }
                }
            }

            String diseaseNameStr = String.join(", ", diseaseNames);

            // Quy tắc: dead > disease > normal
            HealthStatus overallStatus;
            if (deadCount > 0) overallStatus = HealthStatus.DEAD;
            else if (diseaseCount > 0) overallStatus = HealthStatus.DISEASED;
            else overallStatus = HealthStatus.NORMAL;

            GardenHealthEntity gh = GardenHealthEntity.builder()
                    .garden(garden)
                    .normalCell(normalCount)
                    .deadCell(deadCount)
                    .diseaseCell(diseaseCount)
                    .diseaseName(diseaseNameStr)
                    .healthStatus(overallStatus)
                    .build();

            gardenHealthRepository.save(gh);
        }
    }
}
