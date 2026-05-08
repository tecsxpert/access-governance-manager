package com.internship.accessgovernancemanager.service;

import com.internship.accessgovernancemanager.entity.Task;
import com.internship.accessgovernancemanager.entity.UserAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Autowired
    private TemplateEngine templateEngine;

    // 📧 Send simple text email
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    // 📧 Send HTML email using Thymeleaf template
    public void sendHtmlEmail(String to, String subject, String templateName, Context context) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);

            // Process Thymeleaf template
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    // 📧 Send welcome email to new user
    public void sendWelcomeEmail(UserAccess user) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("username", user.getUsername());

        sendHtmlEmail(user.getEmail(), "Welcome to Access Governance Manager",
                     "welcome-email", context);
    }

    // 📧 Send task reminder email
    public void sendTaskReminderEmail(UserAccess user, List<Task> tasks) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("tasks", tasks);
        context.setVariable("taskCount", tasks.size());

        sendHtmlEmail(user.getEmail(), "Task Reminder - Access Governance Manager",
                     "task-reminder", context);
    }

    // 📧 Send deadline alert email
    public void sendDeadlineAlertEmail(UserAccess user, List<Task> tasks) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("tasks", tasks);
        context.setVariable("urgentCount", tasks.size());

        sendHtmlEmail(user.getEmail(), "⚠️ Deadline Alert - Tasks Due Soon",
                     "deadline-alert", context);
    }

    // 📧 Send overdue tasks notification
    public void sendOverdueTasksEmail(UserAccess user, List<Task> tasks) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("tasks", tasks);
        context.setVariable("overdueCount", tasks.size());

        sendHtmlEmail(user.getEmail(), "🚨 Overdue Tasks - Immediate Action Required",
                     "overdue-tasks", context);
    }
}