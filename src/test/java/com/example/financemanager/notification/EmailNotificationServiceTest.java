package com.example.financemanager.notification;

import com.example.financemanager.adapter.EmailNotificationAdapter;
import com.example.financemanager.notification.service.EmailNotificationService;
import com.example.financemanager.utils.model.EmailInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class EmailNotificationServiceTest {

    @Autowired
    private EmailNotificationService emailNotificationService;
    @MockBean
    private EmailNotificationAdapter emailNotificationAdapter;

    @Test
    void sendAlertSuccess() {
        var notificationDto = new EmailInfoDto(
                "joh.doe@gmail.com",
                "Budget Exceed Alert",
                "Your expenses have exceeded the budget limit.");
        emailNotificationService.notify(notificationDto);
        verify(emailNotificationAdapter).sendAlert(notificationDto);
    }
}
