package com.example.demo.gardenLog.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.gardencell.model.GardenCellEntity;
import com.example.demo.reminder.model.ReminderEntity;
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

    @Column(name = "log_type", length = 100, nullable = false)
    String logType; // e.g., REMINDER_COMPLETED, REMINDER_SKIPPED, MANUAL_ENTRY

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "log_date", nullable = false)
    LocalDate logDate;

    @Column(name = "photo_url", length = 512)
    String photoUrl;

    // Quan hệ với bảng gardens
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id", nullable = false)
    @JsonBackReference
    GardenEntity garden;


}
