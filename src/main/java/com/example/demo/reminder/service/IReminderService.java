// src/main/java/com/example/demo/reminder/service/IReminderService.java
package com.example.demo.reminder.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.reminder.controllers.dtos.CreateReminderRequest;
import com.example.demo.reminder.controllers.dtos.UpdateReminderRequest;
import com.example.demo.reminder.controllers.dtos.ReminderResponse;

import java.util.List;

public interface IReminderService {

    /**
     * Tạo mới một Reminder cho user hiện tại.
     *
     * @param dto thông tin để tạo reminder
     * @return ApiResponse chứa ReminderResponse vừa tạo
     */
    ApiResponse<ReminderResponse> createReminder(CreateReminderRequest dto);

    /**
     * Cập nhật một Reminder đã tồn tại (chỉ cập nhật các trường không-null trong DTO).
     *
     * @param dto chứa id và các trường cần thay đổi
     * @return ApiResponse chứa ReminderResponse sau khi cập nhật
     */
    ApiResponse<ReminderResponse> updateReminder(UpdateReminderRequest dto);

    /**
     * Xóa (soft-delete) một Reminder theo id.
     *
     * @param reminderId id của reminder cần xóa
     * @return ApiResponse không có payload
     */
    ApiResponse<Void> deleteReminder(String reminderId);

    /**
     * Lấy danh sách Reminder của user hiện tại, theo gardenId nếu có.
     *
     * @param gardenId (optional) id của vườn để lọc
     * @return ApiResponse chứa danh sách ReminderResponse
     */
    ApiResponse<List<ReminderResponse>> getReminders(String gardenId);


    ApiResponse<ReminderResponse> getReminder(String reminderId);
}
