package com.example.demo.gardenLog.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.enums.ActionType;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.gardencell.model.GardenCellEntity;
import com.example.demo.reminder.model.ReminderEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity(name = "garden_logs")
@Table(name = "garden_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class GardenLogEntity extends BaseEntity {

    // Mô tả ngắn gọn hành động (ghi chú thêm)
    @Column(nullable = false)
    String description;

    // Loại hành động
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 32)
    ActionType actionType;



    // Nếu đến từ reminder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reminder_id")
    ReminderEntity reminder;


    // Giữ quan hệ tới Garden (nếu log mang tính chung toàn vườn)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id", nullable = false)
    @JsonBackReference
    GardenEntity garden;
}
