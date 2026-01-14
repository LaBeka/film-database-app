package com.edu.film_database.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(com.edu.film_database.api.ReviewApi.API_PATH_DICTIONARY)
@Tag(name = "Methods to work with Review", description = com.edu.film_database.api.ReviewApi.API_PATH_DICTIONARY)
@Validated
public interface ReviewApi {

    String API_PATH_DICTIONARY = "/api/review";

}
