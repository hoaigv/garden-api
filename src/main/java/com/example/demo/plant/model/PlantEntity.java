package com.example.demo.plant.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.healthStat.model.HealthStatEntity;
import com.example.demo.gardenLog.model.GardenLogEntity;
import com.example.demo.plantStatus.PlantStatusEntity;
import com.example.demo.plantType.model.PlantTypeEntity;

import com.example.demo.reminder.model.ReminderEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "plants")
@Table(name = "plants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class PlantEntity extends BaseEntity {

    @Column
    LocalDate plantedAt;

    @Column(length = 100)
    String nickname;

    @Column(length = 100)
    String currentStatus; // ví dụ: "Healthy", "Needs Water", ...

    // Quan hệ nhiều cây – 1 vườn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id", nullable = false)
    @JsonBackReference
    GardenEntity garden;

    // Quan hệ nhiều cây – 1 loại cây
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_type_id", nullable = false)
    @JsonBackReference
    PlantTypeEntity plantType;

    // Một cây có nhiều trạng thái (nhật ký tình trạng)
    @OneToMany(mappedBy = "plant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<PlantStatusEntity> plantStatuses = new ArrayList<>();

    // Một cây có nhiều garden_logs (nếu log chỉ rõ cây)
    @OneToMany(mappedBy = "plant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<GardenLogEntity> gardenLogs = new ArrayList<>();

    // Một cây có nhiều health_stats
    @OneToMany(mappedBy = "plant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<HealthStatEntity> healthStats = new ArrayList<>();

    // Một cây có thể có nhiều reminders
    @OneToMany(mappedBy = "plant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<ReminderEntity> reminders = new ArrayList<>();
}
