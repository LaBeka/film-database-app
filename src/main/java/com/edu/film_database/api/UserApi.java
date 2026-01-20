package com.edu.film_database.api;


import com.edu.film_database.dto.request.UserRequestDto;
import com.edu.film_database.dto.request.UserRequestUpdateDto;
import com.edu.film_database.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping(UserApi.API_PATH_DICTIONARY)
@Tag(name = "Methods to work with User", description = UserApi.API_PATH_DICTIONARY)
@Validated
public interface UserApi {

    String API_PATH_DICTIONARY = "/api/user";

    @GetMapping("/get/list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List of all active users. Available for role: ADMIN")
    ResponseEntity<List<UserResponseDto>> getAllUsers();

    @GetMapping("/get/id/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get user by id. Available for role: USER and ADMIN")
    ResponseEntity<UserResponseDto> getUserByID(@Positive @PathVariable("id") int id);

    @GetMapping("/get/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by email. Available for role: ADMIN")
    ResponseEntity<UserResponseDto> getUserByEmail(@Email(message = "Email format is invalid") @PathVariable("email") String email);

    @GetMapping("/getTest/email/{email}")
    @Operation(summary = "Get user by email. Available for role: ADMIN")
    ResponseEntity<UserResponseDto> getUserByEmailTest(@Email(message = "Email format is invalid") @PathVariable("email") String email);

    @PostMapping("/create")
    @Operation(summary = "Create new user with default 'USER' role and send back jwt-token. If email is taken throws Conflict exception. NO ROLE")
    ResponseEntity<String> createNewUser(@Valid @RequestBody UserRequestDto userRequest);

    @PostMapping("/createUser")
    @Operation(summary = "Create new user with default 'USER' role and send back response dto. If email is taken throws Conflict exception. NO ROLE")
    ResponseEntity<UserResponseDto> createNewUserResponseDto(@Valid @RequestBody UserRequestDto userRequest);

    @PutMapping("/update/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Update your own data, throws Conflict exception if you try update someone else's data. Available for role: USER or ADMIN")
    ResponseEntity<UserResponseDto> updateUser(
            @Valid @RequestBody UserRequestUpdateDto userRequest,
            Principal principal);

    @PostMapping("/updateUserToAdmin/{email}")
    @Operation(summary = "Promote OTHER user's 'USER' role to 'ADMIN'. If you try to promote yourself it throws Conflict exception. Available for role: ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<UserResponseDto> updateUserRoleAdmin(
            @Email(message = "Email format is invalid") @PathVariable String email,
            Principal principal);


    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user by email. If you try to remove yourself from db it throws exception. Available for role: ADMIN")
    ResponseEntity<?>  deleteUserByEmail(@Email(message = "Email format is invalid") @PathVariable("email") String email, Principal principal);


    @PostMapping("/login")
    @Operation(summary = "Create token for authentication to log in. NO ROLE.")
    ResponseEntity<?> login(
            @RequestParam @Email(message = "Email format is invalid") String email,
            @RequestParam @Size(min = 3, message = "Password must be at least 3 characters long") String password);

}
