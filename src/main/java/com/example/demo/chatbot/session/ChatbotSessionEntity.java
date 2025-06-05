package com.example.demo.chatbot.session;

import com.example.demo.common.BaseEntity;
import com.example.demo.chatbot.log.ChatbotLogEntity;
import com.example.demo.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "chatbot_sessions")
@Table(name = "chatbot_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class ChatbotSessionEntity extends BaseEntity {

    @Column(nullable = false)
    LocalDateTime sessionStart;

    @Column
    LocalDateTime sessionEnd;


    @Column(columnDefinition = "LONGTEXT")
    String context;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    UserEntity user;


    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<ChatbotLogEntity> logs = new ArrayList<>();
}
