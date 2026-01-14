package com.edu.film_database.service.userService;

import com.edu.film_database.config.JwtUtil;
import com.edu.film_database.dto.request.UserRequestDto;
import com.edu.film_database.dto.response.UserResponseDto;
import com.edu.film_database.model.Role;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.RoleRepository;
import com.edu.film_database.repo.UserRepository;
import com.edu.film_database.service.CustomUserDetailsService;
import com.edu.film_database.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder encoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private CustomUserDetailsService userDetailsService;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private Principal principal;
    private User userUser;
    private User adminUser;
    private Role userRole;
    private Role adminRole;


    @BeforeEach
    public void setup() {
        userRole = Role.builder().id(1L).name("USER").build();
        adminRole = Role.builder().id(2L).name("ADMIN").build();

        userUser = User.builder()
                .id(1).username("testUser").email("test@email.com")
                .password("encoded_pass").currentlyActive(true)
                .roles(Set.of(userRole)).build();

        adminUser = User.builder()
                .id(2).username("adminUser").email("admin@email.com")
                .password("encoded_pass").currentlyActive(true)
                .roles(Set.of(adminRole)).build();

        principal = new UsernamePasswordAuthenticationToken(
                adminUser.getEmail(), null);

    }

//    1. createNewUser(UserRequestDto dto)
//    What to test:
//    Success: Does it save a user with the role "USER"? Does it return a token?
//    Constraint: Does it throw EntityExistsException if the email already exists in the DB?
//    Hashing: Does it call encoder.encode()? (You verify the password was transformed).

    //public UserResponseDto updateUserData(UserRequestDto dto, Principal principal) {}
    // what can be testable in the function?
    // DESCRIPTION Testing the "Only update your own data" rule

    // public UserResponseDto updateUserRole(String email) {}
    // can be testable for the length of the roles, it should be more than 1 role since iam adding new admin role to user role

    //public UserResponseDto deleteUserByEmail(String email, Principal principal) {}
    // can be testable the pricipal does not deactivate himself

    // public String generateToken(String email, String password) {}
    // can be testable if generated token has all valid properties claims, username, authority roles

    //private static UserResponseDto entityToResponseDto(User user, Set<String> stringRoles) {}
    // can be testable for checking if the fileds of the entity is equal to dtos fields

    @Test
    @DisplayName("createNewUser: Should save user with USER role and return token")
    void createNewUser_Success() {
        UserRequestDto dto = new UserRequestDto("newbie", "Fullname User", "new@email.com", "pass",  20);

        Mockito.when(roleRepository.findAll()).thenReturn(List.of(userRole, adminRole));
        Mockito.when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());
        Mockito.when(encoder.encode(anyString())).thenReturn("hashed");

        // capture the user being saved
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Security Mocks
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(auth);
        Mockito.when(jwtUtil.generateToken(any())).thenReturn("fake-jwt-token");

        // Act
        String token = userService.createNewUser(dto);

        // Verification: This is where you check the specific fields checks email, user(default) role, password is hashed and active == true
        Mockito.verify(userRepository).save(argThat(user ->
                user.getEmail().equals("new@email.com") &&
                        user.getRoles().contains(userRole) &&
                        user.getPassword().equals("hashed") &&
                        user.isCurrentlyActive()
        ));
    }

    @Test
    @DisplayName("createNewUser: Should throw EntityExistsException when email is taken")
    void createNewUser_ThrowsException_WhenEmailExists() {
        // 1 Create a DTO with an email that already exists in our "system"
        UserRequestDto dto = new UserRequestDto("newbie", "Fullname User", "existing@email.com", "pass", 20);

        // Create a dummy user object to represent the "existing" person in the DB
        User existingUser = User.builder().email("existing@email.com").build();

        // STUBBING: Tell the Proxy (Mock) to return the existing user instead of Optional.empty()
        Mockito.when(userRepository.findByEmail("existing@email.com"))
                .thenReturn(Optional.of(existingUser));

        // 2. ACT & ASSERT : We expect the call to result in an EntityExistsException
        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> {
            userService.createNewUser(dto);
        });

        // Verify the error message inside the exception is correct
        assertEquals("User with given email already exists, give another email.", exception.getMessage());

        // 3. VERIFY (The "Safety Check")
        // Ensure that userRepository.save() was NEVER called because the code should have stopped
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    // --- 2. updateUserData EXPECTATIONS ---

    @Test
    @DisplayName("updateUserData: Should throw exception when updating someone else's data")
    void updateUserData_Forbidden() {
        UserRequestDto dto = new UserRequestDto("SomeUser", "Hacker", "email@email.com", "pass",  20);
        // Logged user is admin@email.com, trying to update victim@email.com
        Mockito.when(userRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(adminUser));
        Mockito.when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.of(userUser));

        assertThrows(EntityNotFoundException.class, () -> userService.updateUserData(dto, principal));
    }

    @Test
    @DisplayName("updateUserRole: Should increase role count to 2 when adding ADMIN")
    void updateUserRole_Promotion() {
        // 1. Setup: Target user is "user@email.com"
        String targetEmail = "user@email.com";
        String adminEmail = "admin@email.com";

        // The user being promoted
        userUser.setEmail(targetEmail);
        userUser.setCurrentlyActive(true);
        // The admin doing the promotion
        User adminUser = User.builder().email(adminEmail).currentlyActive(true).build();
        Principal tmp_principal = new UsernamePasswordAuthenticationToken(
                adminEmail, null);

        Mockito.when(userRepository.findByEmail(targetEmail)).thenReturn(Optional.of(userUser));
        Mockito.when(userRepository.findByEmail(adminEmail)).thenReturn(Optional.of(adminUser));
        Mockito.when(roleRepository.findAll()).thenReturn(List.of(userRole, adminRole));

        // 3. Act
        UserResponseDto result = userService.updateUserRole(targetEmail, tmp_principal);

        // 4. Assert
        assertNotEquals(adminEmail, result.getEmail());
        assertTrue(result.isCurrentlyActive());
        assertEquals(2, result.getRoles().size());
        assertTrue(result.getRoles().contains("ADMIN"));
        Mockito.verify(userRepository).save(any(User.class));
    }

    // --- 4. deleteUserByEmail EXPECTATIONS --- should look at scennario not to do it second time

    @Test
    @DisplayName("deleteUserByEmail: Should return UserResponseDto")
    void deleteUser_success() {
        // 1. Setup: Target user is "user@email.com"
        String targetEmail = "user@email.com";
        String adminEmail = "admin@email.com";

        // The user being promoted
        userUser.setEmail(targetEmail);
        userUser.setCurrentlyActive(true);
        // The admin doing the promotion
        User adminUser = User.builder().email(adminEmail).currentlyActive(true).build();
        Principal tmp_principal = new UsernamePasswordAuthenticationToken(
                adminEmail, null);

        Mockito.when(userRepository.findByEmail(targetEmail)).thenReturn(Optional.of(userUser));
        Mockito.when(userRepository.findByEmail(adminEmail)).thenReturn(Optional.of(adminUser));
//        Mockito.when(roleRepository.findAll()).thenReturn(List.of(userRole, adminRole));

        // 3. Act
        UserResponseDto result = userService.deleteUserByEmail(targetEmail, tmp_principal);
        // 4. Assert
        assertNotEquals(adminEmail, result.getEmail()); // Correct
        // IMPORTANT: result.isCurrentlyActive() should be FALSE now
        assertFalse(result.isCurrentlyActive(), "User should be deactivated");
        // Verify the DB save was called
        Mockito.verify(userRepository).save(userUser);

        // Check that the object passed to save was indeed deactivated
        assertFalse(userUser.isCurrentlyActive());
    }

    @Test
    @DisplayName("deleteUserByEmail: Should throw exception if user tries to delete themselves")
    void deleteUser_SelfDestructPrevention() {
        String selfEmail = "admin@email.com";

        // Setup: Both the target and the authorized user are the same
        adminUser.setEmail(selfEmail);
        adminUser.setCurrentlyActive(true);

        Principal selfPrincipal = new UsernamePasswordAuthenticationToken(selfEmail, null);

        Mockito.when(userRepository.findByEmail(selfEmail)).thenReturn(Optional.of(adminUser));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userService.deleteUserByEmail(selfEmail, selfPrincipal)
        );

        assertTrue(exception.getMessage().contains("You can not remove yourself"));
    }

    @Test
    @DisplayName("generateToken: Should throw EntityNotFoundException on Auth failure")
    void generateToken_AuthFailure() {
        Mockito.when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Wrong pass"));

        assertThrows(EntityNotFoundException.class, () ->
                userService.generateToken("test@email.com", "wrong")
        );
    }

    // --- 6. getAuthorizedUser (Private method via Public callers) ---

    @Test
    @DisplayName("getAuthorizedUser: Should throw if user is found but inactive in DB")
    void getAuthorizedUser_Inactive() {
        adminUser.setCurrentlyActive(false);
        Mockito.when(userRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(adminUser));

        assertThrows(EntityNotFoundException.class, () ->
                userService.deleteUserByEmail("some@email.com", principal)
        );
    }
}
