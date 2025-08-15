package com.example.demo.plantStage.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.gardencell.model.GardenCellEntity;
import com.example.demo.plantVariety.model.PlantVarietyEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "plant_stage")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class PlantStageEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    String name;

    @Column(nullable = false)
    String iconLink;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    Integer stageOrder;

    @Column(nullable = false)
    Integer estimatedByDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    PlantVarietyEntity plantVariety;

    @OneToMany(mappedBy = "plantStage")
    @JsonManagedReference
    List<GardenCellEntity> gardenCells = new ArrayList<>();


}
