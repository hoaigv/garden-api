package com.example.demo.reminder.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.common.enums.ActionType;
import com.example.demo.common.enums.FrequencyType;
import com.example.demo.common.enums.ReminderStatus;
import com.example.demo.common.enums.ScheduleType;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.garden.repository.GardenRepository;
import com.example.demo.garden.repository.GardenSpecification;
import com.example.demo.gardenLog.service.GardenLogService;
import com.example.demo.gardencell.repository.IGardenCellRepository;
import com.example.demo.reminder.controllers.dtos.CreateReminderRequest;
import com.example.demo.reminder.controllers.dtos.UpdateReminderRequest;
import com.example.demo.reminder.controllers.dtos.ReminderResponse;
import com.example.demo.reminder.mapper.IReminderMapper;
import com.example.demo.reminder.model.ReminderEntity;
import com.example.demo.reminder.repository.IReminderRepository;
import com.example.demo.reminder.repository.ReminderSpecification;
import com.example.demo.reminder.service.IReminderService;
import com.example.demo.user.model.UserEntity;
import com.example.demo.user.repository.IUserRepository;
import com.example.demo.user.repository.UserSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReminderServiceImpl implements IReminderService {
    IReminderRepository reminderRepository;
    IUserRepository userRepository;
    GardenRepository gardenRepository;
    IReminderMapper mapper;
    GardenLogService gardenLogService;

    @Override
    public ApiResponse<ReminderResponse> createReminder(CreateReminderRequest dto) {
        // Find current user
        UserEntity currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        // Map DTO to entity
        ReminderEntity reminder = mapper.toEntity(dto);
        // Manually set enums
        reminder.setActionType(reminder.getActionType());
        reminder.setScheduleType(ScheduleType.valueOf(dto.getScheduleType()));
        reminder.setFrequency(FrequencyType.valueOf(dto.getFrequency()));
        reminder.setTimeOfDay(dto.getTimeOfDay());
        reminder.setDaysOfWeek(dto.getDaysOfWeek());
        reminder.setDayOfMonth(dto.getDayOfMonth());
        reminder.setStatus(dto.getStatus());

        var garden = gardenRepository.findOne(GardenSpecification.hasId(dto.getGardenId()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.GARDEN_NOT_FOUND));
        reminder.setGarden(garden);

        reminder.setUser(currentUser);

        ReminderEntity saved = reminderRepository.save(reminder);
        return ApiResponse.<ReminderResponse>builder()
                .result(mapper.toResponse(saved))
                .build();
    }

    @Override
    public ApiResponse<ReminderResponse> getReminder(String reminderId) {
        var user = userRepository.findOne(UserSpecification.hasEmail(AuthUtils.getUserCurrent()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
        var reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.REMINDER_NOT_FOUND));

        return ApiResponse.<ReminderResponse>builder()
                .result(mapper.toResponse(reminder))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<ReminderResponse> updateReminder(UpdateReminderRequest dto) {
        // 1. Tìm user và reminder như hiện tại
        UserEntity currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        ReminderEntity existing = reminderRepository.findOne(
                (ReminderSpecification.hasId(dto.getId()))
                        .and(ReminderSpecification.hasUserId(currentUser.getId()))
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.REMINDER_NOT_FOUND));

        // 2. Lưu lại status cũ để so sánh
        ReminderStatus oldStatus = existing.getStatus();

        // 3. Map các trường từ DTO
        mapper.updateEntity(dto, existing);
        if (dto.getActionType() != null) existing.setActionType(ActionType.valueOf(dto.getActionType()));
        if (dto.getScheduleType() != null) existing.setScheduleType(ScheduleType.valueOf(dto.getScheduleType()));
        if (dto.getFrequency() != null) existing.setFrequency(FrequencyType.valueOf(dto.getFrequency()));
        if (dto.getTimeOfDay() != null) existing.setTimeOfDay(dto.getTimeOfDay());
        if (dto.getDaysOfWeek() != null) existing.setDaysOfWeek(dto.getDaysOfWeek());
        if (dto.getDayOfMonth() != null) existing.setDayOfMonth(dto.getDayOfMonth());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());

        // 4. Persist reminder
        ReminderEntity updated = reminderRepository.save(existing);

        // 5. Nếu status mới là DONE hoặc SKIPPED, và khác status cũ, sinh log
        ReminderStatus newStatus = updated.getStatus();
        if (newStatus != oldStatus) {
            if (newStatus == ReminderStatus.DONE || newStatus == ReminderStatus.SKIPPED) {
                gardenLogService.recordFromReminder(updated);
            }
        }

        // 6. Trả về response
        return ApiResponse.<ReminderResponse>builder()
                .result(mapper.toResponse(updated))
                .build();
    }


    @Override
    public ApiResponse<Void> deleteReminder(String reminderId) {
        UserEntity currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        Specification<ReminderEntity> spec = ReminderSpecification.hasId(reminderId)
                .and(ReminderSpecification.hasUserId(currentUser.getId()));

        ReminderEntity existing = reminderRepository.findOne(spec)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.REMINDER_NOT_FOUND));

        existing.setDeletedAt(LocalDateTime.now());
        reminderRepository.save(existing);
        return ApiResponse.<Void>builder()
                .message("Reminder deleted successfully")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<ReminderResponse>> getReminders(String gardenId) {
        UserEntity currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        Specification<ReminderEntity> spec = ReminderSpecification.hasUserId(currentUser.getId())
                .and(ReminderSpecification.isNotDelete());
        if (gardenId != null && !gardenId.isBlank()) {
            spec = spec.and(ReminderSpecification.hasGardenId(gardenId));
        }

        List<ReminderResponse> data = new ArrayList<>();
        for (ReminderEntity reminderEntity : reminderRepository.findAll(spec)) {

            ReminderResponse response = mapper.toResponse(reminderEntity);


            data.add(response);
        }

        return ApiResponse.<List<ReminderResponse>>builder()
                .result(data)
                .build();
    }


}
