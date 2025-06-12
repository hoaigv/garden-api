package com.example.demo.user.repository.specification;

import com.example.demo.common.enums.Role;
import com.example.demo.user.model.UserEntity;
import org.springframework.data.jpa.domain.Specification;


public class UserSpecification {
    public static Specification<UserEntity> build(String name, String role, String isActive) {

        Specification<UserEntity> spec = null;

        if (isActive != null) {
            if (isActive.equalsIgnoreCase("true")) {
                spec = isNotDelete();
            } else if (isActive.equalsIgnoreCase("false")) {
                spec = isDeleted();
            }
        }


        if (name != null && !name.isBlank()) {
            spec = spec.and(likeName(name));
        }

        if (role != null && !role.isBlank()) {
            spec = spec.and(hasRole(role));

        }

        return spec;
    }

    public static Specification<UserEntity> isNotDelete() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<UserEntity> hasId(String Id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), Id);
    }

    public static Specification<UserEntity> hasRole(String role) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role);
    }

    public static Specification<UserEntity> isDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("deletedAt"));
    }

    public static Specification<UserEntity> likeName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                root.get("name"), "%" + name + "%"
        );
    }

    public static Specification<UserEntity> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<UserEntity> hasGoogleId(String googleId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("googleId"), googleId);
    }
}
