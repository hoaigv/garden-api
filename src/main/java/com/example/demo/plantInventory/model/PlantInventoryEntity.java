package com.example.demo.plantInventory.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.enums.PlantTypeEnum;
import com.example.demo.gardencell.model.GardenCellEntity;
import com.example.demo.reminder.model.ReminderEntity;
import com.example.demo.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "plant_inventory")
@Table(name = "plant_inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class PlantInventoryEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    UserEntity user;

    @Column(nullable = false)
    String name;

    @Column
    String icon;

    @OneToMany(mappedBy = "plantInventory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<GardenCellEntity> gardenCells = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "plant_type", nullable = false, length = 50)
    PlantTypeEnum plantType;

    @Column(name = "image_url", length = 512)
    String imageUrl;

    @Column(name = "inventory_quantity", nullable = false)
    Integer inventoryQuantity;

    @Column(name = "per_cell_max", nullable = false)
    Integer perCellMax;

    @Column(columnDefinition = "TEXT")
    String description;
}
