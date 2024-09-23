package com.example.financemanager.utils.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("firstName")
        String firstName,

        @JsonProperty("lastName")
        String lastName,

        @JsonProperty("email")
        String email,

        @JsonProperty("userName")
        String userName
) {
}
