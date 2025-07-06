package com.example.demo.notification.scheduler;

import com.example.demo.notification.model.NotificationEntity;
import com.example.demo.notification.service.EmailService;
import com.example.demo.notification.service.NotificationService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final NotificationService notificationService;
    private final EmailService emailService;



    /**
     * Runs every hour on the hour.
     * Cron format: second minute hour day-of-month month day-of-week
     * "0 0 * * * *" = at minute 0 of every hour
     */
    @Scheduled(cron = "0 0 * * * *")
    public void hourlyNotify() {
        LocalDateTime now = LocalDateTime.now();
        // start: đầu giờ hiện tại (ví dụ 14:00 nếu chạy lúc 14:00:00)
        LocalTime startTime = now.withMinute(0).withSecond(0).toLocalTime();
        // end: đầu giờ kế tiếp (ví dụ 15:00)
        LocalTime endTime = now.plusHours(1).withMinute(0).withSecond(0).toLocalTime();

        log.info("Hourly notification dispatch: window {} – {}", startTime, endTime);

        List<NotificationEntity> allNotifications =
                notificationService.buildAllNotification(startTime, endTime);

        for (NotificationEntity notification : allNotifications) {
            try {
                emailService.sendNotificationEmail(notification);
                log.info("Email sent to {} for reminder {}",
                        notification.getUser().getEmail(),
                        notification.getReminder().getTitle());

            } catch (MessagingException ex) {
                log.error("Failed to send email to {}: {}",
                        notification.getUser().getEmail(),
                        ex.getMessage());
            }
        }
    }
}
