package com.example.demo.reminder.mapper;

import com.example.demo.reminder.controllers.dtos.CreateReminderRequest;
import com.example.demo.reminder.controllers.dtos.UpdateReminderRequest;
import com.example.demo.reminder.controllers.dtos.ReminderResponse;
import com.example.demo.reminder.model.ReminderEntity;
import com.example.demo.common.enums.FrequencyType;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface IReminderMapper {


    @Mapping(source = "gardenActivity", target = "garden_activity")
    @Mapping(source = "frequency", target = "frequency", qualifiedByName = "stringToFrequencyType")
    @Mapping(target = "status", source = "status")

    ReminderEntity toEntity(CreateReminderRequest dto);

    @Mapping(source = "task", target = "task")
    @Mapping(source = "gardenActivity", target = "garden_activity")
    @Mapping(source = "specificTime", target = "specificTime")
    @Mapping(source = "frequency", target = "frequency", qualifiedByName = "stringToFrequencyType")
    @Mapping(source = "status", target = "status")

    void updateEntity(UpdateReminderRequest dto, @MappingTarget ReminderEntity entity);

    @Mapping(source = "garden_activity", target = "gardenActivity")
    @Mapping(source = "frequency", target = "frequency", qualifiedByName = "frequencyTypeToString")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "garden.id", target = "gardenId")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "task", target = "task")
    @Mapping(source = "specificTime", target = "specificTime")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    ReminderResponse toResponse(ReminderEntity entity);

    @Named("stringToFrequencyType")
    default FrequencyType stringToFrequencyType(String value) {
        return value != null ? FrequencyType.valueOf(value) : null;
    }

    @Named("frequencyTypeToString")
    default String frequencyTypeToString(FrequencyType type) {
        return type != null ? type.name() : null;
    }
}

