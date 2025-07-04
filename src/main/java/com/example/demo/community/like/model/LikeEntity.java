package com.example.demo.community.like.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.community.post.model.CommunityPostEntity;
import com.example.demo.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "like_post")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class LikeEntity extends BaseEntity {


    // Nhiều comment – 1 post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    CommunityPostEntity post;

    // Nhiều comment – 1 user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    UserEntity user;
}
