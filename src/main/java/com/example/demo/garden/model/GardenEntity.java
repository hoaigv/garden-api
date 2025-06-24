package com.example.demo.garden.model;

import com.example.demo.common.BaseEntity;

import com.example.demo.common.enums.GardenCondition;
import com.example.demo.gardenLog.model.GardenLogEntity;
import com.example.demo.gardenNote.model.GardenNoteEntity;
import com.example.demo.gardencell.model.GardenCellEntity;
import com.example.demo.reminder.model.ReminderEntity;
import com.example.demo.suggestion.model.AIGardenSuggestionEntity;
import com.example.demo.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "gardens")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class GardenEntity extends BaseEntity {

    @Column(length = 255, nullable = false)
    String name;

    @Column(nullable = false)
    Integer rowLength;

    @Column(nullable = false)
    Integer colLength;

    // Tình trạng toàn khu vườn
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    GardenCondition gardenCondition = GardenCondition.NORMAL;

    // Quan hệ nhiều vườn - 1 người dùng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    UserEntity user;

    // Một vườn có nhiều ô trồng
    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<GardenCellEntity> cells = new ArrayList<>();

    // Một vườn có nhiều nhật ký
    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<GardenLogEntity> logs = new ArrayList<>();

    // Một vườn có nhiều nhắc nhở
    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<ReminderEntity> reminders = new ArrayList<>();

    // Một vườn có nhiều ghi chú
    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<GardenNoteEntity> notes = new ArrayList<>();



    // Một vườn có nhiều gợi ý AI
    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<AIGardenSuggestionEntity> aiSuggestions = new ArrayList<>();
}
