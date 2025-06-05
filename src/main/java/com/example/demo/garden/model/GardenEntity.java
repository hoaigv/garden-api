package com.example.demo.garden.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.gardenLog.model.GardenLogEntity;
import com.example.demo.conditionOptimization.model.ConditionOptimizationEntity;
import com.example.demo.plant.model.PlantEntity;

import com.example.demo.reminder.model.ReminderEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "gardens")
@Table(name = "gardens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class GardenEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    String name;

    @Column(length = 255)
    String location;

    @Column(length = 100)
    String soilType;

    @Column(length = 100)
    String sunlight;

    @Column(columnDefinition = "TEXT")
    String description;

    // Quan hệ với User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    com.example.demo.user.model.UserEntity user;

    // Một vườn có nhiều cây
    @OneToMany(mappedBy = "garden", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<PlantEntity> plants = new ArrayList<>();

    // Một vườn có nhiều garden_logs
    @OneToMany(mappedBy = "garden", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<GardenLogEntity> gardenLogs = new ArrayList<>();

    // Một vườn có nhiều condition_optimizations
    @OneToMany(mappedBy = "garden", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<ConditionOptimizationEntity> conditionOptimizations = new ArrayList<>();

    // Một vườn có nhiều reminders (nếu reminder liên quan tới vườn)
    @OneToMany(mappedBy = "garden", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<ReminderEntity> reminders = new ArrayList<>();
}
