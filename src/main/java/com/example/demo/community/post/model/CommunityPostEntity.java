package com.example.demo.community.post.model;

import com.example.demo.common.BaseEntity;
import com.example.demo.community.comment.model.CommentEntity;
import com.example.demo.community.like.model.LikeEntity;
import com.example.demo.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "community_posts")
@Table(name = "community_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class CommunityPostEntity extends BaseEntity {


    @Column(columnDefinition = "TEXT")
    String body;

    @Column
    String imageLink;

    // Nhiều post – 1 user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    UserEntity user;

    // 1 post – n comments
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<CommentEntity> comments = new ArrayList<>();

    // 1 post – n like
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<LikeEntity> likes = new ArrayList<>();
}
