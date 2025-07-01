package com.example.demo.user.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.common.enums.Role;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.user.controllers.dtos.DeleteUsersRequest;
import com.example.demo.user.controllers.dtos.PassUpdateRequest;
import com.example.demo.user.controllers.dtos.RoleUserUpdateRequest;
import com.example.demo.user.controllers.dtos.UserResponse;
import com.example.demo.user.mapper.IUserMapper;
import com.example.demo.user.model.UserEntity;
import com.example.demo.user.repository.IUserRepository;
import com.example.demo.user.repository.UserSpecification;
import com.example.demo.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public abstract class BaseUserService<T> implements IUserService<T> {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserMapper userMapper;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public ApiResponse<Void> updatePassUser(PassUpdateRequest passUpdateRequest) {
        var userCurrent = AuthUtils.getUserCurrent();
        var user = userRepository.findOne(UserSpecification.hasEmail(userCurrent))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        if (user.getGoogleId() != null) {
            user.setPasswordHash(passwordEncoder.encode(passUpdateRequest.getPassword()));
            userRepository.save(user);
            return ApiResponse.<Void>builder()
                    .message("Update Password Success")
                    .build();
        }
        if (!passwordEncoder.matches(passUpdateRequest.getPassword(), user.getPasswordHash())) {
            throw new CustomRuntimeException(ErrorCode.PASSWORD_INCORRECT);
        }
        user.setPasswordHash(passwordEncoder.encode(passUpdateRequest.getPassword()));
        userRepository.save(user);
        return ApiResponse.<Void>builder()
                .message("Update Password Success")
                .build();
    }


    @Override
    public ApiResponse<List<UserResponse>> findAll(Integer page, Integer limit, String name, String role, String isActive, String sortBy, String sortDir) {


        Specification<UserEntity> spec = UserSpecification.build(name, role, isActive);

        // 2. Xác định direction
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // 3. Tạo Pageable với sort
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sortBy));

        // 4. Gọi service
        Page<UserEntity> userPage = userRepository.findAll(spec, pageable);

        List<UserResponse> userResponses = userPage.getContent()
                .stream()
                .map(userMapper::userEntityToUserResponse)
                .toList();

        return ApiResponse.<List<UserResponse>>builder()
                .message("Successfully fetched users")
                .totalPages(userPage.getTotalPages())
                .total(userPage.getTotalElements())
                .result(userResponses)
                .build();
    }

    @Override
    public ApiResponse<Void> deleteUsers(DeleteUsersRequest deleteUsersRequest) {
        for (String id : deleteUsersRequest.getIds()) {
            if (!UserSpecification.hasId(id).toString().isEmpty()) {
                var user = userRepository.findOne(UserSpecification.hasId(id))
                        .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
                user.setDeletedAt(LocalDateTime.now());
                userRepository.save(user);
            }
        }
        return ApiResponse.<Void>builder()
                .message("Successfully deleted users")
                .build();
    }

    @Override
    public ApiResponse<Void> updateRoleUsers(RoleUserUpdateRequest roleUserUpdateRequest) {
        var user = userRepository.findOne(UserSpecification.hasId(roleUserUpdateRequest.getId()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        if (roleUserUpdateRequest.getRole().equals(Role.USER.name())) {
            user.setRole(Role.USER);
        } else {
            user.setRole(Role.ADMIN);
        }
        userRepository.save(user);
        return ApiResponse.<Void>builder()
                .message("Role of " + user.getName() + " updated !")
                .build();
    }
}
