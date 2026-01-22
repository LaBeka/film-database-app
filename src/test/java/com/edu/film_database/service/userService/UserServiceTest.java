package com.edu.film_database.service.userService;

import com.edu.film_database.config.JwtUtil;
import com.edu.film_database.dto.request.UserRequestDto;
import com.edu.film_database.dto.request.UserRequestUpdateDto;
import com.edu.film_database.dto.response.UserResponseDto;
import com.edu.film_database.model.Role;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.RoleRepository;
import com.edu.film_database.repo.UserRepository;
import com.edu.film_database.service.CustomUserDetailsService;
import com.edu.film_database.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        userRole = Role.builder().id(1).name("USER").build();
        adminRole = Role.builder().id(2).name("ADMIN").build();

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
    @AfterEach
    public void tearDown() {
        principal = null;
        userUser = null;
        adminUser = null;
        userRole = null;
        adminRole = null;
    }

    @Test
    @DisplayName("createNewUser: Should save user with USER role and hashed password and return token")
    void createNewUser_Success() {
        UserRequestDto dto = new UserRequestDto("newbie", "Fullname User", "new@email.com", "pass",  20);

        Mockito.when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        Mockito.when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());
        Mockito.when(encoder.encode(anyString())).thenReturn("hashed");

        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserResponseDto result = userService.createNewUser(dto);

        Mockito.verify(userRepository).save(argThat(user ->
                result.getEmail().equals("new@email.com") &&
                        result.getRoles().contains(userRole.getName()) &&
                        result.isCurrentlyActive()
        ));
    }

    @Test
    @DisplayName("createNewUser: Should throw EntityExistsException when email is taken")
    void createNewUser_ThrowsException_WhenEmailExists() {
        UserRequestDto dto = new UserRequestDto("newbie", "Fullname User", "existing@email.com", "pass", 20);

        User existingUser = User.builder().email("existing@email.com").build();

        Mockito.when(userRepository.findByEmail("existing@email.com"))
                .thenReturn(Optional.of(existingUser));

        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> {
            userService.createNewUser(dto);
        });

        assertEquals("User with given email: 'existing@email.com' already exists.", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    // ------------------------------------------------------------------------------------------  updateUserData ---------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("updateUserData: Should update user's data when emails match")
    void updateUserData_Success() {
        UserRequestUpdateDto dto = new UserRequestUpdateDto("newUsername", "New Full Name", adminUser.getEmail(), 25);

        Mockito.when(userRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));

        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserResponseDto result = userService.updateUserData(dto, principal);

        Mockito.verify(userRepository).save(argThat(user ->
                user.getUsername().equals("newUsername") &&
                user.getFullName().equals("New Full Name") &&
                user.getAge() == 25 &&
                user.getEmail().equals(adminUser.getEmail())
        ));
    }

    @Test
    @DisplayName("updateUserData: Should throw exception when updating someone else's data")
    void updateUserData_Forbidden() {
        UserRequestUpdateDto dto = new UserRequestUpdateDto("Should throw exception", "Should throw exception", "test@email.com", 30);
        Mockito.when(userRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(adminUser));
        Mockito.when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(userUser));

        assertThrows(EntityNotFoundException.class, () -> userService.updateUserData(dto, principal));

        // Verify: Confirm that the save method was NEVER reached
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }


    @Test
    @DisplayName("updateUserRole: Should increase role count to 2 when adding ADMIN")
    void updateUserRole_Promotion() {
        String targetEmail = "user@email.com";
        String adminEmail = "admin@email.com";

        userUser.setEmail(targetEmail);
        userUser.setCurrentlyActive(true);
        User adminUser = User.builder().email(adminEmail).currentlyActive(true).build();
        Principal tmp_principal = new UsernamePasswordAuthenticationToken(
                adminEmail, null);

        Mockito.when(userRepository.findByEmail(targetEmail)).thenReturn(Optional.of(userUser));
        Mockito.when(userRepository.findByEmail(adminEmail)).thenReturn(Optional.of(adminUser));
        Mockito.when(roleRepository.findAll()).thenReturn(List.of(userRole, adminRole));

        UserResponseDto result = userService.updateUserRole(targetEmail, tmp_principal);

        assertNotEquals(adminEmail, result.getEmail());
        assertTrue(result.isCurrentlyActive());
        assertEquals(2, result.getRoles().size());
        assertTrue(result.getRoles().contains("ADMIN"));
        Mockito.verify(userRepository).save(any(User.class));
    }
    @Test
    @DisplayName("updateUserRoles : Should rewrite user roles with new list of string roles")
    void updateUserRole_Update_Success() {
        String targetEmail = "test@email.com";
        Set<String> newRolesStrings = Set.of("ADMIN", "USER");

        Mockito.when(userRepository.findByEmail(targetEmail)).thenReturn(Optional.of(userUser));
        Mockito.when(userRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));

        Mockito.when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        Mockito.when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserResponseDto result = userService.updateUserRoles(targetEmail, newRolesStrings, adminUser.getEmail());

        assertNotNull(result);
        assertEquals(newRolesStrings.size(), result.getRoles().size());
        Mockito.verify(userRepository).save(argThat(u -> u.getRoles().size() == 2));

        assertTrue(result.isCurrentlyActive());
        assertTrue(result.getRoles().contains("ADMIN") || result.getRoles().contains("USER"),
                "User should have either ADMIN or USER role");
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("updateUserRoles: Should throw exception when admin tries to update own roles")
    void updateUserRoles_Fail_SelfUpdate() {
        String email = adminUser.getEmail();
        Set<String> roles = Set.of("USER");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(adminUser));

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> userService.updateUserRoles(email, roles, email));

        assertTrue(ex.getMessage().contains("can not promote his own role"));
        Mockito.verify(userRepository, Mockito.never()).save(any());
    }
    @Test
    @DisplayName("updateUserRoles: Should throw exception if target user is inactive")
    void updateUserRoles_Fail_InactiveUser() {
        // Arrange
        userUser.setCurrentlyActive(false); // Make the user "deleted"
        Mockito.when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(userUser));

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> userService.updateUserRoles("test@email.com", Set.of("ADMIN"), adminUser.getEmail()));
    }

    @Test
    @DisplayName("updateUserRoles: Should throw exception if a provided role name does not exist")
    void updateUserRoles_Fail_InvalidRole() {
        // Arrange
        String targetEmail = "test@email.com";
        Set<String> roles = Set.of("NON_EXISTENT_ROLE");

        Mockito.when(userRepository.findByEmail(targetEmail)).thenReturn(Optional.of(userUser));
        Mockito.when(userRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));

        // Mock roleRepository to return empty for the fake role
        Mockito.when(roleRepository.findByName("NON_EXISTENT_ROLE")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> userService.updateUserRoles(targetEmail, roles, adminUser.getEmail()));

        assertTrue(ex.getMessage().contains("does not exist in the database"));
    }
    // --- 4. deleteUserByEmail EXPECTATIONS --- should look at scenario not to do it second time

    @Test
    @DisplayName("deleteUserByEmail: Should return UserResponseDto")
    void deleteUser_success() {
        String targetEmail = "user@email.com";
        String adminEmail = "admin@email.com";

        userUser.setEmail(targetEmail);
        userUser.setCurrentlyActive(true);
        // The admin doing the promotion
        User adminUser = User.builder().email(adminEmail).currentlyActive(true).build();
        Principal tmp_principal = new UsernamePasswordAuthenticationToken(
                adminEmail, null);

        Mockito.when(userRepository.findByEmail(targetEmail)).thenReturn(Optional.of(userUser));
        Mockito.when(userRepository.findByEmail(adminEmail)).thenReturn(Optional.of(adminUser));

        UserResponseDto result = userService.deleteUserByEmail(targetEmail, tmp_principal);
        assertNotEquals(adminEmail, result.getEmail()); // Correct
        assertFalse(result.isCurrentlyActive(), "User should be deactivated");
        Mockito.verify(userRepository).save(userUser);

        assertFalse(userUser.isCurrentlyActive());
    }

    @Test
    @DisplayName("deleteUserByEmail: Should throw exception if user tries to delete themselves")
    void deleteUser_SelfDestructPrevention() {
        String selfEmail = "admin@email.com";

        adminUser.setEmail(selfEmail);
        adminUser.setCurrentlyActive(true);

        Principal selfPrincipal = new UsernamePasswordAuthenticationToken(selfEmail, null);

        Mockito.when(userRepository.findByEmail(selfEmail)).thenReturn(Optional.of(adminUser));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userService.deleteUserByEmail(selfEmail, selfPrincipal)
        );

        assertTrue(exception.getMessage().contains("You can not remove yourself"));
    }

    @Test
    @DisplayName("generateToken: Should return a valid JWT when credentials are correct")
    void generateToken_Success() {
        String email = "test@email.com";
        String password = "password123";
        String mockToken = "mocked-jwt-token";

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(email);
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);

        UserDetails mockUserDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetailsService.loadUserByUsername(email))
                .thenReturn(mockUserDetails);

        Mockito.when(jwtUtil.generateToken(mockUserDetails))
                .thenReturn(mockToken);
        String resultToken = userService.generateToken(email, password);

        assertEquals(mockToken, resultToken);
    }

    @Test
    @DisplayName("generateToken: Should throw EntityNotFoundException on Auth failure")
    void generateToken_AuthFailure() {
        Mockito.when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Wrong pass"));

        assertThrows(UsernameNotFoundException.class, () ->
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

    @Test
    @DisplayName("entityToResponseDto: Should correctly map all fields from User to DTO")
    void entityToResponseDto_Success() {
        UserResponseDto result = userService.entityToResponseDto(userUser);

        assertNotNull(result, "Resulting DTO should not be null");
        assertEquals(userUser.getId(), result.getId());
        assertEquals(userUser.getUsername(), result.getUserName());
        assertEquals(userUser.getEmail(), result.getEmail());
        assertEquals(userUser.getFullName(), result.getFullName()); // Assuming testUser has fullName set in setup if applicable, otherwise verifies null matches null
        assertEquals(userUser.isCurrentlyActive(), result.isCurrentlyActive());
        assertEquals(1, result.getRoles().size());
        assertTrue(result.getRoles().contains("USER"));
    }

    @Test
    @DisplayName("entityToResponseDto: Should map inactive user status, empty roles, nullable (username, full name, age) correctly")
    void entityToResponseDto_InactiveUser() {
        User inactiveUser = User.builder()
                .id(3)
                .username(null)
                .email("inactive@email.com")
                .fullName(null)
                .currentlyActive(false) // Explicitly false
                .roles(null)
                .build();
        Set<String> roleNames = Set.of("USER");

        UserResponseDto result = userService.entityToResponseDto(inactiveUser);

        assertFalse(result.isCurrentlyActive());
        assertNotNull(result.getRoles(), "Roles set should never be null");
        assertTrue(result.getRoles().isEmpty(), "Roles set should be empty");
        assertNull(result.getFullName());
        assertNull(result.getUserName());
    }

}
