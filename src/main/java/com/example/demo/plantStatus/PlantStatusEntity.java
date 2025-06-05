package com.example.demo.plantStatus;

import com.example.demo.common.BaseEntity;
import com.example.demo.plant.model.PlantEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity(name = "plant_statuses")
@Table(name = "plant_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class PlantStatusEntity extends BaseEntity {

    @Column(nullable = false)
    LocalDate statusDate;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(length = 50)
    String healthCondition; // ví dụ: "Tốt", "Trung bình", "Xấu"

    @Column(length = 100)
    String pestInfo; // ví dụ: "Rệp, Nhện…"

    @Column(columnDefinition = "VARCHAR(2083)")
    String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    @JsonBackReference
    PlantEntity plant;
}
