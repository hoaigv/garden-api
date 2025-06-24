package com.example.demo.reminder.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.enums.FrequencyType;
import com.example.demo.common.enums.ReminderStatus;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.gardenLog.model.GardenLogEntity;
import com.example.demo.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    String task;   // tên hoạt động, ví dụ: "Tưới nước"

    @Column
    String garden_activity;

    @Column
    LocalDateTime specificTime;  // thời gian cụ thể nếu áp dụng một lần

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    FrequencyType frequency;    // tần suất: DAILY, WEEKLY, etc.

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    ReminderStatus status; // PENDING | DONE | SKIPPED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id", nullable = false)
    @JsonBackReference
    GardenEntity garden;

    @OneToMany(mappedBy = "reminder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<GardenLogEntity> gardenLogs = new HashSet<>();
}
