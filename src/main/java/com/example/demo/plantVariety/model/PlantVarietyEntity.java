package com.example.demo.plantVariety.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.enums.PlantTypeEnum;
import com.example.demo.gardencell.model.GardenCellEntity;
import com.example.demo.plantInventory.model.PlantInventoryEntity;
import com.example.demo.plantStage.model.PlantStageEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "plant_varieties")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class PlantVarietyEntity extends BaseEntity {

    @Column(nullable = false)
    String name;

    @Column
    String description;

    @Column(nullable = false)
    String iconLink;

    @OneToMany(mappedBy = "plantVariety", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<GardenCellEntity> gardenCells = new ArrayList<>();

    @OneToMany(mappedBy = "plantVariety", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<PlantStageEntity> stages = new ArrayList<>();

    @OneToMany(mappedBy = "plantVariety", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<PlantInventoryEntity> inventory = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    PlantTypeEnum plantType;
}
