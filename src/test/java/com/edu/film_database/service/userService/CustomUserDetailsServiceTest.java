package com.edu.film_database.service.userService;

import com.edu.film_database.model.Role;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.UserRepository;
import com.edu.film_database.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User userEntity;
    private Role role;

    @BeforeEach
    public void setup() {
        role = Role.builder().id(1).name("USER").build();
        userEntity = User.builder()
                .id(1).username("testUser").email("test@email.com")
                .password("encoded_pass").currentlyActive(true)
                .roles(Set.of(role))
                .build();
    }
    @Test
    @DisplayName("loadUserByUsername: Success - Should map Entity to Spring Security User")
    void loadUserByUsername_Success() {
        Mockito.when(userRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.of(userEntity));

        UserDetails result = customUserDetailsService.loadUserByUsername("test@email.com");

        assertNotNull(result);
        assertEquals(userEntity.getEmail(), result.getUsername());
        assertEquals(userEntity.getPassword(), result.getPassword());

        boolean hasUserAuthority = result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER"));
        assertTrue(hasUserAuthority, "Should contain USER authority");
    }

    @Test
    @DisplayName("loadUserByUsername: Failure - Should throw Exception when email not found")
    void loadUserByUsername_NotFound() {
        Mockito.when(userRepository.findByEmail("unknown@email.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("unknown@email.com");
        });
    }
}
