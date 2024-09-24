package com.example.financemanager.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractNotificationAdapter<T> {

    protected final RestTemplate restTemplate;

    @Value("${notification.api.base-url}")
    protected String notificationApiBaseUrl;

    protected AbstractNotificationAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public abstract void sendAlert(T notificationDto);
}
