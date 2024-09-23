package com.example.financemanager.adapter;

import com.example.financemanager.utils.model.EmailInfoDto;
import com.example.financemanager.utils.model.ResponseEnvelope;
import com.example.financemanager.utils.model.exception.NotificationNotSentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class EmailNotificationAdapter extends NotificationAdapter<EmailInfoDto> {

    protected EmailNotificationAdapter(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public void sendAlert(EmailInfoDto notificationDto) {
        log.debug("Sending email notification to user");
        var url = notificationApiBaseUrl + "/mail";
        var result = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(notificationDto),
                new ParameterizedTypeReference<ResponseEnvelope<Void>>() {
                }
        );

        if (HttpStatus.NO_CONTENT != result.getStatusCode()) {
            log.warn("Failed to send email notification for user with email: {}", notificationDto.recipientEmail());
            throw new NotificationNotSentException("Failed to send notification for user with email: " +
                    notificationDto.recipientEmail());
        }
        log.debug("Successfully sent email notification to user");
    }

}
