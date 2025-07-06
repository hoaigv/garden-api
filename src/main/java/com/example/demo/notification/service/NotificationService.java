package com.example.demo.notification.service;

import com.example.demo.common.enums.FrequencyType;
import com.example.demo.common.enums.ReminderStatus;
import com.example.demo.common.enums.WeekDay;
import com.example.demo.notification.model.NotificationEntity;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.reminder.model.ReminderEntity;
import com.example.demo.reminder.repository.IReminderRepository;
import com.example.demo.reminder.repository.ReminderSpecification;
import com.example.demo.user.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {

    IReminderRepository reminderRepository;
    NotificationRepository notificationRepository;
    IUserRepository userRepository;

    public List<NotificationEntity> buildAllNotification(LocalTime startTime, LocalTime endTime) {

        WeekDay todayWeekDay = WeekDay.valueOf(LocalDate.now().getDayOfWeek().name());
        Integer todayDayOfMonth = LocalDate.now().getDayOfMonth();


        var reminders = reminderRepository.findAll(

                ReminderSpecification.timeOfDayBetween(startTime, endTime)
                        .and(

                                ReminderSpecification.hasFrequency(FrequencyType.DAILY)

                                        .or(
                                                ReminderSpecification.hasFrequency(FrequencyType.WEEKLY)
                                                        .and(ReminderSpecification.hasWeekDay(todayWeekDay))
                                        )

                                        .or(
                                                ReminderSpecification.hasFrequency(FrequencyType.MONTHLY)
                                                        .and(ReminderSpecification.hasDayOfMonth(todayDayOfMonth))
                                        )
                        ).and(ReminderSpecification.isNotDelete())
        );


        List<NotificationEntity> notifications = new ArrayList<>();
        for (ReminderEntity r : reminders) {
            r.setStatus(ReminderStatus.PENDING);
            reminderRepository.save(r);
            var userID = r.getUser().getId();
            var user = userRepository.findById(userID)
                    .orElseThrow();
            var n = NotificationEntity.builder()
                    .build();
            n.setUser(user);
            n.setTitle(r.getTitle() + LocalDate.now());
            n.setReminder(r);
            var entity = notificationRepository.save(n);
            notifications.add(entity);
        }

        return notifications;
    }
}

