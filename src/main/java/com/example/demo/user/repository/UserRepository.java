package com.example.demo.user.repository;


import com.example.demo.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    @Query("SELECT u FROM users u WHERE u.email = :email AND u.deletedAt IS NULL")
    Optional<UserEntity> findByEmailAndNotDeleted(@Param("email") String email);


    @Query(
            "select case when count(u) > 0 then true else false end from users u where u.deletedAt is  null and u.googleId = :googleId")
    boolean findGoogleIdActive(@Param(value = "googleId") String googleId);


}
