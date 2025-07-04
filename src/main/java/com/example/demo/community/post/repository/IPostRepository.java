package com.example.demo.community.post.repository;

import com.example.demo.community.post.model.CommunityPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostRepository extends JpaRepository<CommunityPostEntity, String>, JpaSpecificationExecutor<CommunityPostEntity> {
    // Additional query methods (if any) can be defined here
}
