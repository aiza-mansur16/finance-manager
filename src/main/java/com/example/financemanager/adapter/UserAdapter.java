package com.example.financemanager.adapter;

import com.example.financemanager.utils.model.ResponseEnvelope;
import com.example.financemanager.utils.model.UserDto;
import com.example.financemanager.utils.model.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class UserAdapter extends Adapter<UserDto> {


    public UserAdapter(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public UserDto getInfo(String url) {
        log.debug("Fetching user info from user service");
        var result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseEnvelope<UserDto>>() {
                }
        ).getBody();

        if (result != null) {
            log.debug("User info successfully fetched.");
            return result.data();
        } else {
            log.warn("User info not found.");
            throw new UserNotFoundException("User not found.");
        }
    }
}
