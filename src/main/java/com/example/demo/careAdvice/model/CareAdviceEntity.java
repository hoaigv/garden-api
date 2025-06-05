package com.example.demo.careAdvice.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.plantType.model.PlantTypeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "care_advices")
@Table(name = "care_advices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class CareAdviceEntity extends BaseEntity {

    @Column(length = 100)
    String stage; // ví dụ: "Gieo hạt", "Ra hoa", "Thu hoạch", ...

    @Column(columnDefinition = "TEXT")
    String adviceText;

    // Nhiều careAdvice – 1 loại cây
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_type_id", nullable = false)
    @JsonBackReference
    PlantTypeEntity plantType;
}
