package com.edu.film_database.controller;

import com.edu.film_database.api.UserApi;
import com.edu.film_database.dto.request.UserRequestDto;
import com.edu.film_database.dto.request.UserRequestUpdateDto;
import com.edu.film_database.dto.response.UserResponseDto;
import com.edu.film_database.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> response = userService.getAllUser();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponseDto> getUserByID(int id) {
        UserResponseDto response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponseDto> getUserByEmail(String email) {
        UserResponseDto response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponseDto> createNewUser(UserRequestDto userRequest) {
        UserResponseDto response = userService.createNewUser(userRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponseDto> updateUser(
            UserRequestUpdateDto userRequest,
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

    @Override
    public ResponseEntity<UserResponseDto> updateRoles(String email, Set<String> roles, Principal principal) {
        UserResponseDto updatedUser = userService.updateUserRoles(email, roles, principal.getName());
        return ResponseEntity.ok(updatedUser);
    }
}
