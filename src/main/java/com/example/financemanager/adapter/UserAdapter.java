package com.example.financemanager.adapter;

import com.example.financemanager.utils.model.ResponseEnvelope;
import com.example.financemanager.utils.model.UserDto;
import com.example.financemanager.utils.model.exception.UserNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class UserAdapter extends AbstractAdapter<UserDto> {


    public UserAdapter(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @CircuitBreaker(name = "UserAdapter")
    @Retry(name = "RetryUserAdapter")
    @Override
    public UserDto getInfo(String url) {
        if (log.isDebugEnabled()) {
            log.debug("Fetching user info from user service with url: " + url);
        }
        var result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseEnvelope<UserDto>>() {
                }
        ).getBody();

        if (result != null) {
            if (log.isDebugEnabled()) {
                log.debug("User info successfully fetched.");
            }
            return result.data();
        } else {
            if (log.isWarnEnabled()) {
                log.warn("User info not found.");
            }
            throw new UserNotFoundException("User not found");
        }
    }
}
