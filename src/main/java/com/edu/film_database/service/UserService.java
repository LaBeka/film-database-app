package com.edu.film_database.service;

import com.edu.film_database.config.JwtUtil;
import com.edu.film_database.dto.request.UserRequestDto;
import com.edu.film_database.dto.request.UserRequestUpdateDto;
import com.edu.film_database.dto.response.UserResponseDto;
import com.edu.film_database.model.Role;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.RoleRepository;
import com.edu.film_database.repo.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    private User getAuthorizedUser(String email) {
        Optional<User> principalFromDB = userRepository.findByEmail(email);
        if(principalFromDB.isEmpty()) {
            throw new EntityNotFoundException("Logged user not found");
        }
        if(!principalFromDB.get().isCurrentlyActive()){
            throw new EntityNotFoundException("Logged user is removed from database");
        }
        return principalFromDB.get();
    }

    public List<UserResponseDto> getAllUser() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(u -> u.isCurrentlyActive())
                .map(u ->  entityToResponseDto(u))
                .toList();
    }

    public UserResponseDto getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User by provided id not found"));
        return entityToResponseDto(user);
    }

    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User by provided email not found"));
        return entityToResponseDto(user);
    }

    public UserResponseDto updateUserData(UserRequestUpdateDto dto, Principal principal) {
        User authorizedUser = getAuthorizedUser(principal.getName());
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User by provided email not found"));

        if(!authorizedUser.getEmail().equals(dto.getEmail())) {
            throw new EntityNotFoundException("Authenticated user's email does not match with provided email. You can update only your own data.");        }

        if(!user.isCurrentlyActive()) {
            throw new EntityNotFoundException("User is removed from db.");
        }
        user.setUsername(dto.getUserName());
        user.setFullName(dto.getFullName());
        user.setAge(dto.getAge());
        user.setCurrentlyActive(true);
        userRepository.save(user);

        return entityToResponseDto(user);
    }

    public UserResponseDto updateUserRole(String email, Principal principal) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User by provided email not found"));
        User authorizedUser = getAuthorizedUser(principal.getName());

        if(authorizedUser.getEmail().equals(user.getEmail())) {
            throw new EntityNotFoundException("Authenticated user can not promote his own role.");
        }
        if(!user.isCurrentlyActive()) {
            throw new EntityNotFoundException("User is removed from db.");
        }

        Set<Role> roles = new HashSet<>(user.getRoles());
        if(roles.size() == 1){
            Role admin = roleRepository.findAll()
                            .stream()
                                    .filter(r -> r.getName().equals("ADMIN"))
                                    .findFirst()
                                    .get();
            roles.add(admin);

            user.setRoles(roles);
            userRepository.save(user);
        }
        return entityToResponseDto(user);
    }

    public UserResponseDto deleteUserByEmail(String email, Principal principal) {
        User authorizedUser = getAuthorizedUser(principal.getName());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User by provided email not found"));

        if(!user.isCurrentlyActive()) {
            throw new EntityNotFoundException("User is already removed from db.");
        }
        if(user.getEmail().equals(authorizedUser.getEmail())) {
            throw new EntityNotFoundException("Provided email matches with logged user's email. You can not remove yourself from db.");
        }
        user.setCurrentlyActive(false);
        userRepository.save(user);

        return entityToResponseDto(user);
    }

    public String generateToken(String email, String password) {
//        System.out.println("From userservice: password: " +  password);
        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException e) {
            throw new UsernameNotFoundException("User not found by given email: '" + email + "' and password: '" + password +"'");
        }
        UserDetails user = userDetailsService.loadUserByUsername(auth.getName());
        String token = jwtUtil.generateToken(user);

        return token;
    }

    public UserResponseDto entityToResponseDto(User user) {
        Set<String> stringRoles = Optional.ofNullable(user.getRoles()) // Handles null
                .orElse(Collections.emptySet()) // Uses empty set if null
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .currentlyActive(user.isCurrentlyActive())
                .age(user.getAge())
                .roles(stringRoles)
                .build();
    }

    public UserResponseDto createNewUser(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EntityExistsException("User with given email already exists.");
        }
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new EntityNotFoundException("Default Role 'USER' not found in database"));

        User newUser = User.builder()
                .fullName(dto.getFullName())
                .username(dto.getUserName())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .age(dto.getAge())
                .currentlyActive(true)
                .roles(Set.of(userRole))
                .build();
        User savedUser = userRepository.save(newUser);

        return entityToResponseDto(savedUser);
    }
}
