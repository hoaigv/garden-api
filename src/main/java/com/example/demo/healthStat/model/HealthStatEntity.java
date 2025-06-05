package com.example.demo.healthStat.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.plant.model.PlantEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity(name = "health_stats")
@Table(name = "health_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class HealthStatEntity extends BaseEntity {

    @Column(nullable = false)
    LocalDate recordDate;

    @Column
    Integer healthScore; // 0 – 100

    @Column(columnDefinition = "TEXT")
    String notes;

    // Nhiều healthStat – 1 cây
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    @JsonBackReference
    PlantEntity plant;
}
