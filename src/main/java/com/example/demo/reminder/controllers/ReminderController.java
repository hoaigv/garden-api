// src/main/java/com/example/demo/reminder/controllers/ReminderController.java
package com.example.demo.reminder.controllers;

import com.example.demo.common.ApiResponse;
import com.example.demo.reminder.controllers.dtos.CreateReminderRequest;
import com.example.demo.reminder.controllers.dtos.ReminderResponse;
import com.example.demo.reminder.controllers.dtos.UpdateReminderRequest;
import com.example.demo.reminder.service.IReminderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReminderController {

    IReminderService reminderService;

    /**
     * Lấy danh sách reminders, có thể filter theo gardenId
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReminderResponse>>> findAll(
            @RequestParam(required = false) String gardenId
    ) {
        ApiResponse<List<ReminderResponse>> response = reminderService.getReminders(gardenId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{reminderId}")
    public ResponseEntity<ApiResponse<ReminderResponse>> findBYId(
            @PathVariable String reminderId

    ) {
        var response = reminderService.getReminder(reminderId);
        return ResponseEntity.ok(response);
    }


    /**
     * Tạo mới reminder
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReminderResponse>> createReminder(
            @RequestBody @Valid CreateReminderRequest request
    ) {
        ApiResponse<ReminderResponse> response = reminderService.createReminder(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Cập nhật reminder theo id
     */
    @PutMapping("/{reminderId}")
    public ResponseEntity<ApiResponse<ReminderResponse>> updateReminder(
            @PathVariable String reminderId,
            @RequestBody @Valid UpdateReminderRequest request
    ) {
        // đảm bảo id path khớp id trong body
        request.setId(reminderId);
        ApiResponse<ReminderResponse> response = reminderService.updateReminder(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Xóa (soft-delete) reminder theo id
     */
    @DeleteMapping("/{reminderId}")
    public ResponseEntity<ApiResponse<Void>> deleteReminder(
            @PathVariable String reminderId
    ) {
        ApiResponse<Void> response = reminderService.deleteReminder(reminderId);
        return ResponseEntity.ok(response);
    }
}
