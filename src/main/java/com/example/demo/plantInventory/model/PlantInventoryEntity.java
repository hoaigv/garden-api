package com.example.demo.plantInventory.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.plantVariety.model.PlantVarietyEntity;
import com.example.demo.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "plant_inventory")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class PlantInventoryEntity extends BaseEntity {

    @Column(nullable = false)
    Integer numberOfVariety = 0;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn
    PlantVarietyEntity plantVariety;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    UserEntity user;
}
