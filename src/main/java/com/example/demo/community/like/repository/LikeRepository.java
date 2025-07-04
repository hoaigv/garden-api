package com.example.demo.community.like.repository;

import com.example.demo.community.like.model.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, String>, JpaSpecificationExecutor<LikeEntity> {
    // Bạn có thể thêm các method truy vấn tùy chỉnh nếu cần
}
