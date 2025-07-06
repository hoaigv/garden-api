package com.example.demo.reminder.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.enums.*;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.notification.model.NotificationEntity;
import com.example.demo.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    String title;   // tên hoạt động, ví dụ: "Tưới nước"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    ActionType actionType;   // loại hành động (ví dụ: WATERING, PRUNING, ...)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ScheduleType scheduleType;

    // Nếu FIXED
    @Column
    private LocalDateTime fixedDateTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    FrequencyType frequency;    // tần suất: DAILY, WEEKLY, etc.


    // time of day for  RECURRING
    @Column
    LocalTime timeOfDay;

    // Chỉ dùng cho WEEKLY: danh sách ngày trong tuần
    @ElementCollection(targetClass = WeekDay.class)
    @CollectionTable
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private List<WeekDay> daysOfWeek;

    // Chỉ dùng cho MONTHLY: 1..31 hoặc -1 (cuối tháng)
    @Column
    private Integer dayOfMonth;

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
    List<NotificationEntity> notifications = new ArrayList<>();
}
