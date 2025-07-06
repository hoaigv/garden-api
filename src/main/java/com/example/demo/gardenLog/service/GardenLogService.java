package com.example.demo.gardenLog.service;

import com.example.demo.common.enums.ReminderStatus;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.gardenLog.controller.dto.GardenLogResponse;
import com.example.demo.gardenLog.mapper.IGardenLogMapper;
import com.example.demo.gardenLog.model.GardenLogEntity;
import com.example.demo.gardenLog.repository.IGardenLogRepository;
import com.example.demo.reminder.model.ReminderEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenLogService {
    IGardenLogRepository logRepository;
    IGardenLogMapper mapper;

    /**
     * Tạo GardenLogEntity tự động khi có thao tác trên Reminder.
     */

    public void recordFromReminder(ReminderEntity rem) {
        GardenLogEntity log = GardenLogEntity.builder()
                .garden(rem.getGarden())                       // vườn liên quan
                .reminder(rem)                                 // nguồn reminder
                .actionType(rem.getActionType())               // cùng actionType
                .description(buildDescription(rem))            // mô tả
                .build();

        logRepository.save(log);
    }

    private String buildDescription(ReminderEntity rem) {
        String verb = rem.getStatus() == ReminderStatus.DONE ? "Completed" : "Skipped";
        return String.format("Auto-log: %s \"%s\"", verb, rem.getTitle());
    }

    public List<GardenLogResponse> getLogs(String gardenId) {
        List<GardenLogEntity> entities = logRepository.findByGardenId(gardenId)
                .orElseThrow(()-> new CustomRuntimeException(ErrorCode.GARDEN_NOT_FOUND));

        return entities.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}

