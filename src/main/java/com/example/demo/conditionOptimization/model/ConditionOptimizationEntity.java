package com.example.demo.conditionOptimization.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.garden.model.GardenEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "condition_optimizations")
@Table(name = "condition_optimizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class ConditionOptimizationEntity extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    String soilSuggestion;

    @Column(columnDefinition = "TEXT")
    String sunlightSuggestion;

    @Column(columnDefinition = "TEXT")
    String temperatureSuggestion;

    // Nhiều conditionOptimization – 1 vườn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id", nullable = false)
    @JsonBackReference
    GardenEntity garden;
}
