package com.example.demo.chatbot.log;

import com.example.demo.common.BaseEntity;
import com.example.demo.chatbot.session.ChatbotSessionEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "chatbot_logs")
@Table(name = "chatbot_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class ChatbotLogEntity extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    String userMessage;

    @Column(columnDefinition = "TEXT")
    String botResponse;

    // Nhiều log – 1 session
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonBackReference
    ChatbotSessionEntity session;
}
