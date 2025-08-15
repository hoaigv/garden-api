package com.example.demo.gardencell.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.enums.HealthStatus;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.plantStage.model.PlantStageEntity;
import com.example.demo.plantVariety.model.PlantVarietyEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "garden_cells")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class GardenCellEntity extends BaseEntity {

    @Column(nullable = false)
    Integer rowIndex;

    @Column(nullable = false)
    Integer colIndex;

    @Column(nullable = false)
    Short quantity = 0;

    @Column
    String diseaseName;

    @Column(nullable = false)
    String imgCellCurrent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    @JsonIgnoreProperties("gardenCells")
    PlantStageEntity plantStage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    @JsonIgnoreProperties("gardenCells")
    PlantVarietyEntity plantVariety;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    HealthStatus healthStatus = HealthStatus.NORMAL;

    // Quan hệ nhiều ô - 1 vườn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    GardenEntity garden;
}
