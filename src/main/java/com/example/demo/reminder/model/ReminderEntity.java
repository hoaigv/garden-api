package com.example.demo.reminder.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.plant.model.PlantEntity;
import com.example.demo.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity(name = "reminders")
@Table(name = "reminders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class ReminderEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    String task;   // ví dụ: "Tưới nước", "Bón phân", ...

    @Column(length = 50)
    String frequency; // ví dụ: "Hàng ngày", "Hàng tuần", ...

    @Column
    LocalDate nextDueDate;

    @Column(length = 20)
    String status; // ví dụ: "PENDING", "DONE"

    // Quan hệ nhiều reminder – 1 user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    UserEntity user;

    // Quan hệ nhiều reminder – 1 cây (có thể null nếu reminder liên quan chung cho vườn)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @JsonBackReference
    PlantEntity plant;

    // Quan hệ nhiều reminder – 1 vườn (có thể null nếu reminder chỉ cho 1 cây)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id")
    @JsonBackReference
    GardenEntity garden;
}
