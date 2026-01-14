package com.edu.film_database.service;

import ch.qos.logback.core.joran.spi.ActionException;
import com.edu.film_database.config.JwtUtil;
import com.edu.film_database.dto.request.UserRequestDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Logged user not found"));
    }

    public List<UserResponseDto> getAllUser(Principal principal) {
        getAuthorizedUser(principal.getName());

        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(u -> u.isCurrentlyActive())
                .map(u -> {
                    // Convert Set<Role> to Set<String>
                    Set<String> stringRoles = u.getRoles().stream()
                            .map(role -> role.getName()) // or role.getAuthority(), etc.
                            .collect(Collectors.toSet());
                    return entityToResponseDto(u, stringRoles);
                })
                .toList();
    }

    public UserResponseDto getUserById(int id, Principal principal) {
        getAuthorizedUser(principal.getName());

        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User by provided id not found"));

        Set<String> stringRoles = user.getRoles().stream()
                .map(role -> role.getName()) // or role.getAuthority(), etc.
                .collect(Collectors.toSet());

        return entityToResponseDto(user, stringRoles);
    }

    public UserResponseDto getUserByEmail(String email, Principal principal) {
        getAuthorizedUser(principal.getName());

        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User by provided email not found"));

        Set<String> stringRoles = user.getRoles().stream()
                .map(role -> role.getName()) // or role.getAuthority(), etc.
                .collect(Collectors.toSet());

        return entityToResponseDto(user, stringRoles);
    }

    public String createNewUser(UserRequestDto dto) {
        Set<Role> userRole = roleRepository.findAll().stream().filter(r -> r.getName().equals("USER")).collect(Collectors.toSet());

        Optional<User> user = userRepository.findByEmail(dto.getEmail());

        if(user.isEmpty()) {
            User newUser = User.builder()
                    .username(dto.getUsername())
                    .email(dto.getEmail())
                    .password(encoder.encode(dto.getPassword()))
                    .fullName(dto.getFullName())
                    .age(dto.getAge())
                    .currentlyActive(true)
                    .roles(userRole)
                    .build();
            User saved = userRepository.save(newUser);
            user = Optional.of(saved);
        } else {
            throw new EntityExistsException("User with given email already exists, give another email.");
        }

        return generateToken(user.get().getEmail(), dto.getPassword());
    }

    public UserResponseDto updateUserData(UserRequestDto dto, Principal principal) {
        getAuthorizedUser(principal.getName());
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User by provided email not found"));

        if(!principal.getName().equals(dto.getEmail())) {
            throw new EntityNotFoundException("You can update only your own data. Authenticated user's email does not match with provided email");
        }
        Set<String> stringRoles = user.getRoles().stream()
                .map(role -> role.getName()) // or role.getAuthority(), etc.
                .collect(Collectors.toSet());

        if(!user.isCurrentlyActive()) {
            throw new EntityNotFoundException("User is removed from db.");
        }
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setFullName(dto.getFullName());
        user.setAge(dto.getAge());
        user.setCurrentlyActive(true);
        userRepository.save(user);

        return entityToResponseDto(user, stringRoles);
    }

    public UserResponseDto updateUserRole(String email, Principal principal) {
        getAuthorizedUser(principal.getName());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User by provided email not found"));

        if(!user.isCurrentlyActive()) {
            throw new EntityNotFoundException("User is removed from db.");
        }

        Set<Role> roles = user.getRoles();
        if(roles.size() == 1){
            Role admin = roleRepository.findAll().stream().filter(r -> r.getName().equals("ADMIN")).findAny().get();
            roles.add(admin);

            user.setRoles(roles);
            userRepository.save(user);
        }

        Set<String> stringRoles = user.getRoles().stream()
                .map(role -> role.getName()) // or role.getAuthority(), etc.
                .collect(Collectors.toSet());

        return entityToResponseDto(user, stringRoles);
    }

    public UserResponseDto deleteUserByEmail(String email, Principal principal) {
        getAuthorizedUser(principal.getName());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User by provided email not found"));

        if(!user.isCurrentlyActive()) {
            throw new EntityNotFoundException("User is already removed from db.");
        }
        user.setCurrentlyActive(false);
        userRepository.save(user);
        Set<String> stringRoles = user.getRoles().stream()
                .map(role -> role.getName()) // or role.getAuthority(), etc.
                .collect(Collectors.toSet());
        return entityToResponseDto(user, stringRoles);
    }

    public String generateToken(String email, String password) {
        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException e) {
            throw new EntityNotFoundException("User not found");
        }
        UserDetails user = userDetailsService.loadUserByUsername(auth.getName());
        String token = jwtUtil.generateToken(user);

        return token;
    }


    private static UserResponseDto entityToResponseDto(User user, Set<String> stringRoles) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .currentlyActive(user.isCurrentlyActive())
                .roles(stringRoles)
                .build();
    }

}
