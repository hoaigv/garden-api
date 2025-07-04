package com.example.demo.reminder.controllers;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.annotation.positiveOrDefault.PositiveOrDefault;
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
     * Get paged list of reminders with optional filters.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReminderResponse>>> findAll(
            @PositiveOrDefault int page,
            @PositiveOrDefault(defaultValue = 10) int size,
            @RequestParam(required = false) String gardenId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String frequency,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        ApiResponse<List<ReminderResponse>> response = reminderService.findAll(
                page, size, gardenId, status, frequency, sortBy, sortDir
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get all reminders for the current authenticated user for each garden.
     */
    @GetMapping("{gardenId}/me")
    public ResponseEntity<ApiResponse<List<ReminderResponse>>> findAllForCurrentUser(
            @PathVariable String gardenId
    ) {
        ApiResponse<List<ReminderResponse>> response = reminderService.findAllForCurrentUser(gardenId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all reminders for the current authenticated user.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<ReminderResponse>>> findAllForCurrentUser() {
        ApiResponse<List<ReminderResponse>> response = reminderService.findAllForUser();
        return ResponseEntity.ok(response);
    }


    /**
     * Get all reminders for the current authenticated user.
     */
    @GetMapping("/{reminderId}")
    public ResponseEntity<ApiResponse<ReminderResponse>> findOneById(
            @PathVariable String reminderId
    ) {
        var response = reminderService.findReminderById(reminderId);
        return ResponseEntity.ok(response);
    }


    /**
     * Create a new reminder.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createReminder(
            @RequestBody @Valid CreateReminderRequest request
    ) {
        ApiResponse<Void> response = reminderService.createReminder(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing reminder.
     */
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateReminder(
            @RequestBody @Valid UpdateReminderRequest request
    ) {
        ApiResponse<Void> response = reminderService.updateReminder(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete one or more reminders by ID.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteReminders(
            @RequestBody @Valid List<String> reminderIds
    ) {
        ApiResponse<Void> response = reminderService.deleteReminders(reminderIds);
        return ResponseEntity.ok(response);
    }
}
