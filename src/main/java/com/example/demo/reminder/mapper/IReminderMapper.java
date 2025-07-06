package com.example.demo.reminder.mapper;

import com.example.demo.reminder.model.ReminderEntity;
import com.example.demo.reminder.controllers.dtos.CreateReminderRequest;
import com.example.demo.reminder.controllers.dtos.UpdateReminderRequest;
import com.example.demo.reminder.controllers.dtos.ReminderResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IReminderMapper {

    /**
     * Map trực tiếp các trường giống tên. Các trường enum sẽ được set thủ công trong service.
     */

    ReminderEntity toEntity(CreateReminderRequest dto);

    /**
     * Cập nhật một ReminderEntity đã có sẵn.
     * Bỏ qua các trường null trong DTO (nullValuePropertyMappingStrategy = IGNORE).
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "garden.id", source = "dto.gardenId")
    void updateEntity(UpdateReminderRequest dto, @MappingTarget ReminderEntity existing);

    @Mapping(target = "gardenId", source = "garden.id")
    @Mapping(target = "gardenName" ,source ="garden.name")
    ReminderResponse toResponse(ReminderEntity entity);

    /**
     * Chuyển một list entity sang list response.
     */
    List<ReminderResponse> toResponseList(List<ReminderEntity> entities);
}
