package com.example.financemanager.adapter;

import org.springframework.web.client.RestTemplate;

public abstract class AbstractAdapter<T> {
  protected final RestTemplate restTemplate;

  protected AbstractAdapter(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public abstract T getInfo(String url);
}
