package com.example.demo.gardenHealth.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.enums.HealthStatus;
import com.example.demo.garden.model.GardenEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class GardenHealthEntity extends BaseEntity {

    /**
     * Tham chiếu tới Garden mà bản ghi này ghi nhận.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    GardenEntity garden;


    @Column(nullable = false)
    Integer normalCell;

    @Column(nullable = false)
    Integer deadCell;

    @Column(nullable = false)
    Integer diseaseCell;


    /**
     * Trạng thái sức khỏe của cả vườn tại thời điểm ghi nhận.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    HealthStatus healthStatus;


}
