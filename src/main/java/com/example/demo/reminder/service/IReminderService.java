package com.example.demo.reminder.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.reminder.controllers.dtos.CreateReminderRequest;
import com.example.demo.reminder.controllers.dtos.UpdateReminderRequest;
import com.example.demo.reminder.controllers.dtos.ReminderResponse;

import java.util.List;

/**
 * Service interface for managing Reminder entities.
 */
public interface IReminderService {

    /**
     * Retrieve a paged and optionally filtered list of reminders.
     *
     * @param page      zero-based page index
     * @param size      page size
     * @param gardenId  optional garden ID filter
     * @param status    optional reminder status filter
     * @param frequency optional frequency filter (ONE_TIME, DAILY, WEEKLY, MONTHLY)
     * @param sortBy    field to sort by
     * @param sortDir   sort direction ("asc" or "desc")
     */
    ApiResponse<List<ReminderResponse>> findAll(
            Integer page,
            Integer size,
            String gardenId,
            String status,
            String frequency,
            String sortBy,
            String sortDir
    );

    /**
     * Retrieve the full list of reminders for the currently authenticated user.
     *
     * @return ApiResponse containing list of ReminderResponse for current user
     */
    ApiResponse<List<ReminderResponse>> findAllForCurrentUser();

    /**
     * Create a new reminder entry.
     */
    ApiResponse<Void> createReminder(CreateReminderRequest request);

    /**
     * Update an existing reminder entry.
     */
    ApiResponse<Void> updateReminder(UpdateReminderRequest request);

    /**
     * Delete one or more reminders by ID.
     */
    ApiResponse<Void> deleteReminders(List<String> reminderIds);


    ApiResponse<ReminderResponse> findReminderById (String reminderId);
}
