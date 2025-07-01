package com.example.demo.gardenNote.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "garden_notes")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class GardenNoteEntity extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    String noteText;

    @Column(nullable = false, length = 512)
    String noteTitle;


    @Column(length = 512)
    String photoUrl;

    // Một ghi chú thuộc về 1 khu vườn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    GardenEntity garden;

    // Một ghi chú được viết bởi 1 người dùng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    UserEntity user;
}
