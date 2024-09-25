package com.example.financemanager.adapter;

import com.example.financemanager.utils.model.ResponseEnvelope;
import com.example.financemanager.utils.model.UserDto;
import com.example.financemanager.utils.model.exception.ThirdPartyServiceUnavailableException;
import com.example.financemanager.utils.model.exception.UserNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = {
    "SIC_INNER_SHOULD_BE_STATIC_ANON"
}, justification = "The rest template is being referred from abstract adapter in all methods.")

public class UserAdapter extends AbstractAdapter<UserDto> {

  protected UserAdapter(RestTemplate restTemplate) {
    super(restTemplate);
  }

  @CircuitBreaker(name = "UserAdapter")
  @Retry(name = "RetryUserAdapter")
  @Override
  public UserDto getInfo(String url) {
    try {
      return getUserDto(url);
    } catch (HttpClientErrorException e) {

      if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
        throw new UserNotFoundException("User not found", e);
      } else {
        throw new ThirdPartyServiceUnavailableException("External service not unavailable", e);
      }
    }
  }

  private UserDto getUserDto(String url) {

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

    if (result != null && result.data() != null) {
      return result.data();
    }

    throw new UserNotFoundException("User not found");
  }

}
