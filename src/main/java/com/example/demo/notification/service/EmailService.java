package com.example.demo.notification.service;

import com.example.demo.notification.model.NotificationEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final Environment env;

    /**
     * Gửi email notification sử dụng Thymeleaf template 'notification-email.html'.
     */
    public void sendNotificationEmail(NotificationEntity entity) throws MessagingException {
        // Chuẩn bị dữ liệu cho template
        Context ctx = new Context();
        ctx.setVariable("title", entity.getReminder().getTitle());
        ctx.setVariable("scheduledTime", entity.getReminder().getTimeOfDay());
        ctx.setVariable("userName", entity.getUser().getName());

        // Render HTML từ template
        String html = templateEngine.process("notification-email", ctx);

        // Tạo và gửi MIME message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        helper.setTo(entity.getUser().getEmail());
        helper.setSubject("Garden Reminder: " + entity.getReminder().getTitle());
        helper.setText(html, true);

        mailSender.send(message);
        log.info("Notification email sent to {} for reminder {}",
                entity.getUser().getEmail(), entity.getReminder().getTitle());
    }
}