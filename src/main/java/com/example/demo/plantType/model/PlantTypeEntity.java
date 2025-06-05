package com.example.demo.plantType.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.careAdvice.model.CareAdviceEntity;
import com.example.demo.plant.model.PlantEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "plant_types")
@Table(name = "plant_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class PlantTypeEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(length = 100)
    String optimalSoil;

    @Column(length = 100)
    String optimalSunlight;

    @Column(length = 100)
    String optimalTemperature;

    // Mối quan hệ 1 loại cây – n cây thực tế
    @OneToMany(mappedBy = "plantType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<PlantEntity> plants = new ArrayList<>();

    // Mối quan hệ lời khuyên – gắn vào plantType
    @OneToMany(mappedBy = "plantType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<CareAdviceEntity> careAdvices = new ArrayList<>();
}
