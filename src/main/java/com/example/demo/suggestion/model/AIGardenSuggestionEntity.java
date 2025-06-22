package com.example.demo.suggestion.model;


import com.example.demo.common.BaseEntity;
import com.example.demo.garden.model.GardenEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "ai_garden_suggestions")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class AIGardenSuggestionEntity extends BaseEntity {

    @Column(nullable = false)
    String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    GardenEntity garden;
}
