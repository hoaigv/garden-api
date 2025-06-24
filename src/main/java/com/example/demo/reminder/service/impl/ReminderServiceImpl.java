// src/main/java/com/example/demo/reminder/service/impl/ReminderServiceImpl.java
package com.example.demo.reminder.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.common.enums.ReminderStatus;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.garden.repository.GardenRepository;
import com.example.demo.garden.repository.GardenSpecification;
import com.example.demo.reminder.controllers.dtos.CreateReminderRequest;
import com.example.demo.reminder.controllers.dtos.UpdateReminderRequest;
import com.example.demo.reminder.controllers.dtos.ReminderResponse;
import com.example.demo.reminder.mapper.IReminderMapper;
import com.example.demo.reminder.model.ReminderEntity;
import com.example.demo.reminder.repository.IReminderRepository;
import com.example.demo.reminder.repository.ReminderSpecification;
import com.example.demo.reminder.service.IReminderService;
import com.example.demo.user.model.UserEntity;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReminderServiceImpl implements IReminderService {

    IReminderRepository reminderRepository;
    IReminderMapper reminderMapper;
    UserRepository userRepository;
    GardenRepository gardenRepository;

    @Override
    public ApiResponse<List<ReminderResponse>> findAll(
            Integer page,
            Integer size,
            String gardenId,
            String status,
            String frequency,
            String sortBy,
            String sortDir
    ) {
        // xác thực người dùng hiện tại
        UserEntity currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        // build specification sử dụng ReminderSpecification
        Specification<ReminderEntity> spec = ReminderSpecification.build(
                null,
                gardenId,
                status,
                frequency,
                null,
                null
        ).and((root, query, cb) -> cb.equal(root.get("user").get("id"), currentUser.getId()));

        // pageable
        Sort.Direction dir = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        Page<ReminderEntity> pageResult = reminderRepository.findAll(spec, pageable);

        List<ReminderResponse> dtos = pageResult.getContent().stream()
                .map(reminderMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<ReminderResponse>>builder()
                .message("Successfully fetched reminders")
                .result(dtos)
                .total(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<List<ReminderResponse>> findAllForCurrentUser() {
        UserEntity currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        Specification<ReminderEntity> spec = (root, query, cb) -> cb.equal(root.get("user").get("id"), currentUser.getId());
        List<ReminderResponse> dtos = reminderRepository.findAll(spec).stream()
                .map(reminderMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<ReminderResponse>>builder()
                .message("Fetched reminders for current user")
                .result(dtos)
                .build();
    }

    @Override
    public ApiResponse<ReminderResponse> findReminderById(String reminderId) {
        var entity = reminderRepository.findOne(ReminderSpecification.hasId(reminderId))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        var resp = reminderMapper.toResponse(entity);
        return ApiResponse.<ReminderResponse>builder()
                .result(resp)
                .message("Fetched reminder")
                .build();
    }

    @Override
    public ApiResponse<Void> createReminder(CreateReminderRequest request) {
        var currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        var garden = gardenRepository.findOne(GardenSpecification.hasId(request.getGardenId()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.GARDEN_NOT_FOUND));
        ReminderEntity entity = reminderMapper.toEntity(request);
        entity.setUser(currentUser);
        entity.setGarden(garden);
        reminderRepository.save(entity);

        return ApiResponse.<Void>builder()
                .message("Reminder created successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> updateReminder(UpdateReminderRequest request) {
        ReminderEntity existing = reminderRepository.findById(request.getId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));
        reminderMapper.updateEntity(request, existing);
        reminderRepository.save(existing);

        return ApiResponse.<Void>builder()
                .message("Reminder updated successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> deleteReminders(List<String> reminderIds) {
        for (String id : reminderIds) {
            if (reminderRepository.existsById(id)) {
                reminderRepository.deleteById(id);
            } else {
                throw new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND);
            }
        }
        return ApiResponse.<Void>builder()
                .message("Reminders deleted successfully")
                .build();
    }
}