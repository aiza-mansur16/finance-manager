package com.example.financemanager.notification.service;

import com.example.financemanager.adapter.EmailNotificationAdapter;
import com.example.financemanager.utils.model.EmailInfoDto;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements Notification<EmailInfoDto> {
    private final EmailNotificationAdapter emailNotificationAdapter;

    public EmailNotificationService(EmailNotificationAdapter emailNotificationAdapter) {
        this.emailNotificationAdapter = emailNotificationAdapter;
    }

    @Override
    public void notify(EmailInfoDto notification) {
        emailNotificationAdapter.sendAlert(notification);
    }
}
