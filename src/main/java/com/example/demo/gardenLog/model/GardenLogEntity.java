package com.example.demo.gardenLog.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.plant.model.PlantEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity(name = "garden_logs")
@Table(name = "garden_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class GardenLogEntity extends BaseEntity {

    @Column(nullable = false, length = 50)
    String logType; // ví dụ: "Tưới nước", "Bón phân", ...

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(nullable = false)
    LocalDate logDate;

    @Column(columnDefinition = "VARCHAR(2083)")
    String photoUrl;

    // Nhiều log – 1 vườn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id", nullable = false)
    @JsonBackReference
    GardenEntity garden;

    // Nhiều log – 1 cây (có thể null nếu log chung cho vườn)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @JsonBackReference
    PlantEntity plant;
}
