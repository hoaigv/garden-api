package com.example.demo.gardenActivity.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.reminder.model.ReminderEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "garden_activities")
@Table(name = "garden_activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class GardenActivityEntity extends BaseEntity {

    @Column(length = 100, nullable = false)
    String name;

    @Column(length = 500)
    String description;

    @Column
    String icon;


    @OneToOne(mappedBy = "garden_activity")
    ReminderEntity reminder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    GardenEntity garden;
}
