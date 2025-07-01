package com.example.demo.user.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.chatbot.session.model.ChatbotSessionEntity;
import com.example.demo.common.enums.Role;
import com.example.demo.community.comment.model.CommentEntity;
import com.example.demo.community.post.model.CommunityPostEntity;


import com.example.demo.garden.model.GardenEntity;
import com.example.demo.reminder.model.ReminderEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    String name;

    @Column(nullable = false, unique = true)
    String email;

    /**
     * Nếu user đăng bằng Google OAuth, trường này có thể để null.
     * Nếu user đăng ký bằng email/password, lưu bcrypt hash ở đây.
     */
    @Column
    String passwordHash;

    /**
     * Nếu đăng nhập bằng Google, lưu googleId (sub) ở đây; null nếu user chưa từng OAuth Google.
     */
    @Column(unique = true)
    String googleId;

    /**
     * Link tới avatar (nếu user upload hoặc dùng avatar từ Google profile).
     */
    @Column(columnDefinition = "VARCHAR(2083)")
    String avatarLink;

    /**
     * Refresh token (nếu bạn triển khai refresh JWT riêng cái này).
     */
    @Column(columnDefinition = "TEXT")
    String refreshToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Role role;

    @Column
    String location;


    // Một user có thể có nhiều vườn
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<GardenEntity> gardens = new ArrayList<>();

    // Một user có thể có nhiều reminder
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<ReminderEntity> reminders = new ArrayList<>();

    // Một user có nhiều bài viết cộng đồng
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<CommunityPostEntity> communityPosts = new ArrayList<>();

    // Một user có nhiều bình luận
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<CommentEntity> comments = new ArrayList<>();

    // Một user có nhiều phiên chat với chatbot
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<ChatbotSessionEntity> chatbotSessions = new ArrayList<>();
}
