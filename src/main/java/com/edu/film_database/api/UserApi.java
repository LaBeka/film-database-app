package com.edu.film_database.api;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(UserApi.API_PATH_DICTIONARY)
@Tag(name = "Methods to work with User", description = UserApi.API_PATH_DICTIONARY)
@Validated
public interface UserApi {

    String API_PATH_DICTIONARY = "/api/user";
}
