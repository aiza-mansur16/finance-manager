package com.example.financemanager.adapter;

import com.example.financemanager.utils.model.UserDto;
import com.example.financemanager.utils.model.exception.ThirdPartyServiceUnavailableException;
import com.example.financemanager.utils.model.exception.UserNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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
        try {
            if (log.isDebugEnabled()) {
                log.debug("Fetching user info from user service with url: " + url);
            }
            return restTemplate.getForObject(
                    url,
                    UserDto.class
            );

        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                throw new UserNotFoundException("User not found", e);
            } else {
                throw new ThirdPartyServiceUnavailableException("External service not unavailable", e);
            }
        }
    }
}
