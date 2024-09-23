package com.example.financemanager.adapter;

import org.springframework.web.client.RestTemplate;

public abstract class Adapter<T> {
    protected final RestTemplate restTemplate;

    protected Adapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public abstract T getInfo(String url);
}
