package com.edu.film_database.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserResponseDto {

    private int id;
    private String userName;
    private String fullName;
    private String email;
    private boolean currentlyActive;
    private Set<String> roles;
}
