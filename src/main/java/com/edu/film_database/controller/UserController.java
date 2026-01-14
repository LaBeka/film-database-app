package com.edu.film_database.controller;

import com.edu.film_database.api.UserApi;
import com.edu.film_database.dto.request.UserRequestDto;
import com.edu.film_database.dto.response.UserResponseDto;
import com.edu.film_database.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<List<UserResponseDto>> getAllUsers(Principal principal) {
        List<UserResponseDto> response = userService.getAllUser(principal);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponseDto> getUserByID(int id, Principal principal) {
        UserResponseDto response = userService.getUserById(id, principal);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponseDto> getUserByEmail(String email, Principal principal) {
        UserResponseDto response = userService.getUserByEmail(email, principal);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> createNewUser(UserRequestDto userRequest) {
        String response = userService.createNewUser(userRequest);
        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<UserResponseDto> updateUser(
            UserRequestDto userRequest,
            Principal principal) {
        UserResponseDto response = userService.updateUserData(userRequest, principal);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponseDto> updateUserRoleAdmin(String email, Principal principal) {
        UserResponseDto response = userService.updateUserRole(email, principal);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> deleteUserByEmail(String email, Principal principal) {
        UserResponseDto response = userService.deleteUserByEmail(email, principal);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> login(String email, String password) {
        String response = userService.generateToken(email, password);
        return  ResponseEntity.ok(response);
    }
}
