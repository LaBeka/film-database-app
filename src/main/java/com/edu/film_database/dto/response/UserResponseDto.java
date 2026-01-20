package com.edu.film_database.dto.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private int id;
    private String userName;
    private String fullName;
    private String email;
    private boolean currentlyActive;
    private int age;
    private Set<String> roles;
}
