package com.edu.film_database.controller;

import com.edu.film_database.api.UserApi;
import com.edu.film_database.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

}
