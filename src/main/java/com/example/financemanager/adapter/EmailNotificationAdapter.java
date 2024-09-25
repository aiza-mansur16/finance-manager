package com.example.financemanager.adapter;

import com.example.financemanager.utils.model.EmailInfoDto;
import com.example.financemanager.utils.model.ResponseEnvelope;
import com.example.financemanager.utils.model.exception.NotificationNotSentException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class EmailNotificationAdapter extends AbstractNotificationAdapter<EmailInfoDto> {

  protected EmailNotificationAdapter(RestTemplate restTemplate) {
    super(restTemplate);
  }

  @CircuitBreaker(name = "EmailNotificationAdapter")
  @Retry(name = "RetryEmailNotificationAdapter")
  @Override
  public void sendAlert(EmailInfoDto notificationDto) {
    try {
      if (log.isDebugEnabled()) {
        log.debug("Sending email notification to user");
      }
      var url = notificationApiBaseUrl + "/mail";
      restTemplate.postForObject(
          url,
          notificationDto,
          ResponseEnvelope.class
      );
      if (log.isDebugEnabled()) {
        log.debug("Successfully sent email notification to user");
      }
    } catch (HttpClientErrorException e) {
      if (log.isWarnEnabled()) {
        log.warn("Failed to send email notification for user with email: {}", notificationDto.recipientEmail());
      }
      throw new NotificationNotSentException("Failed to send notification for user with email: " +
          notificationDto.recipientEmail(), e);
    }
  }

}
