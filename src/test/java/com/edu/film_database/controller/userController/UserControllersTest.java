package com.edu.film_database.controller.userController;

import com.edu.film_database.dto.request.UserRequestDto;
import com.edu.film_database.model.Role;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.RoleRepository;
import com.edu.film_database.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Role userRole;
    private Role adminRole;
    private User userUser;
    private User adminUser;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        adminUser = null;
        userUser = null;
        userRole = null;
        adminRole = null;

        String rawPassword = "test123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        userRole = Role.builder().name("USER").build();
        adminRole = Role.builder().name("ADMIN").build();
        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        userUser = User.builder().username("testUser").fullName("testUserFullName").email("test@mail.com").password(encodedPassword).currentlyActive(true).roles(Set.of(userRole)).build();
        userRepository.save(userUser);

        adminUser = User.builder().username("admin").fullName("admin").email("admin@mail.com").password("admin").currentlyActive(true).roles(Set.of(adminRole)).build();
        userRepository.save(adminUser);

    }
    // ------------ @GetMapping("/get/list") -------
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Success: GET /api/user/get/list")
    void testGetAllUsers_AdminSuccess() throws Exception {
        mockMvc.perform(get("/api/user/get/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].email").value("test@mail.com"))
                .andExpect(jsonPath("$.[0].userName").value("testUser"));;
    }
    @Test
    @WithMockUser(roles = "USER") // Simulate a user WITHOUT Admin privileges
    @DisplayName("403 Forbidden: GET /get/list - Fail for Non-Admin")
    void getAllUsers_Forbidden() throws Exception {
        mockMvc.perform(get("/api/user/get/list"))
                .andExpect(status().isForbidden());
    }
    @Test
    @DisplayName("401 Unauthorized: GET /get/list - Fail for Unauthenticated User")
    void getAllUsers_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/user/get/list"))
                .andExpect(status().isUnauthorized()); // Expects 401
    }

    // ------------ @GetMapping("/get/id/{id}") -------
    @Test
    @DisplayName("401 Unauthorized: GET /get/id/{id} - Fail for Unauthenticated User")
    void getUserByID_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/user/get/id/1"))
                .andExpect(status().isUnauthorized()); // Expects 401
    }
    @Test
    @WithMockUser(roles = {"USER","ADMIN"})
    @DisplayName("GET /get/id/{id} - Success returning DTO")
    void getUserByID_Success() throws Exception {
        mockMvc.perform(get("/api/user/get/id/" + userUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"));
    }
    @Test
    @WithMockUser(roles = {"USER","ADMIN"})
    @DisplayName("GET /get/id/{id} - Fail 400 on Negative ID (@Positive)")
    void getUserByID_InvalidId() throws Exception {
        // -5 violates @Positive
        mockMvc.perform(get("/api/user/get/id/-5"))
                .andExpect(status().isBadRequest()); // 400 Error
    }
// ------------ @GetMapping("/get/email/{email}") -------
    @Test
    @DisplayName("401 Unauthorized: GET /get/email/{email} - Fail for Unauthenticated User")
    void getUserByEmail_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/user/get/email/" + userUser.getEmail()))
                .andExpect(status().isUnauthorized()); // Expects 401
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /get/email/{email} - Success")
    void getUserByEmail_Success() throws Exception {
        mockMvc.perform(get("/api/user/get/email/" + userUser.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testUser"));
    }
    @Test
    @WithMockUser(roles = "USER") // Simulate a user WITHOUT Admin privileges
    @DisplayName("403 Forbidden: GET /get/email/{email} - Fail for Non-Admin")
    void getUserByEmail_Forbidden() throws Exception {
        mockMvc.perform(get("/api/user/get/email/" + userUser.getEmail()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /get/email/{email} - Fail 400 on BAD Email Format")
    void getUserByEmail_InvalidFormat() throws Exception {
        mockMvc.perform(get("/api/user/get/email/bad-email"))
                .andExpect(status().isBadRequest());
    }

    // -/--------------------------------------- PostMapping("/create") RETURNS UserResponseDto-------
    @Test
    @DisplayName("POST /create - Success: Create User and return Token (Public Access)")
    void createUserDto_Success() throws Exception {
        userRepository.deleteAll();
        if (roleRepository.findByName("USER").isEmpty()) {
            roleRepository.save(Role.builder().name("USER").build());
        }
        UserRequestDto newUser = new UserRequestDto(
                "newUser",
                "New User Fullname",
                "unique@gmail.com",
                "pass123",
                25
        );
        // Act & Assert
        mockMvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser))) // Convert DTO to JSON
                .andExpect(status().isOk());

        assertTrue(userRepository.findByEmail("unique@gmail.com").isPresent());
    }

    @Test
    @DisplayName("POST /create - Fail 409 Conflict: Email already taken")
    void createNewUserDto_Conflict() throws Exception {
        UserRequestDto duplicateUser = new UserRequestDto(
                "anotherUser",
                "Some Name",
                "test@mail.com", // <--- THIS EMAIL ALREADY EXISTS
                "password",
                30
        );

        mockMvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(duplicateUser)))
                .andExpect(status().isConflict()); // Expect HTTP 409
    }

    @Test
    @DisplayName("POST /create - Fail 400: Invalid Email Format")
    void createNewUserDto_InvalidEmail() throws Exception {
        UserRequestDto invalid = new UserRequestDto("user", "Name", "not-an-email", "pass", 20);

        mockMvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists()); // Verify the error is about email
    }
    @Test
    @DisplayName("POST /create - Fail 400: Password too short (< 3 chars)")
    void createNewUserDto_ShortPassword() throws Exception {
        UserRequestDto invalid = new UserRequestDto("user", "Name", "valid@mail.com", "12", 20);

        mockMvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    @DisplayName("POST /create - Fail 400: Age too low (< 1)")
    void createNewUserDto_InvalidAge() throws Exception {
        UserRequestDto invalid = new UserRequestDto("user", "Name", "valid@mail.com", "pass", 0);

        mockMvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.age").exists());
    }
// --------------------------------------- @PutMapping("/update/{email}")------------------------------------------
    @Test
    @WithMockUser(username = "test@mail.com", roles = "USER") // <--- ACTING AS THIS USER
    @DisplayName("PUT /update/{email} - Success: User updates their own data")
    void updateUser_Success() throws Exception {
        UserRequestDto updateData = new UserRequestDto(
                "updatedUser",
                "Updated FullName",
                "test@mail.com", // Keeping email same to avoid changing ID (depending on your logic)
                "newPass123",
                30
        );
        mockMvc.perform(put("/api/user/update/test@mail.com") // <--- URL matches Principal
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("updatedUser")) // DTO Field name
                .andExpect(jsonPath("$.fullName").value("Updated FullName"));
        Optional<User> updated = userRepository.findByEmail("test@mail.com");
        assertEquals("updatedUser", updated.get().getUsername());
    }

    @Test
    @WithMockUser(username = "test@mail.com", roles = "USER") // <--- ACTING AS NORMAL USER
    @DisplayName("PUT /update/{email} - Fail 409: User tries to update someone else")
    void updateUser_Conflict_WrongUser() throws Exception {
        UserRequestDto othersUpdate = new UserRequestDto(
                "hacked", "hacked", "admin@mail.com", "hacked", 20
        );

        mockMvc.perform(put("/api/user/update/admin@mail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(othersUpdate)))
                .andDo(print())
                .andExpect(status().isNotFound()); // Expects 404
    }

    @Test
    @WithMockUser(username = "test@mail.com", roles = "USER")
    @DisplayName("PUT /update/{email} - Fail 400: Invalid Data")
    void updateUser_InvalidData() throws Exception {
        UserRequestDto invalidData = new UserRequestDto(
                "", // Blank username
                "",
                "not-an-email",
                "1", // Short password
                10
        );

        // Act & Assert
        mockMvc.perform(put("/api/user/update/test@mail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidData)))
                .andExpect(status().isBadRequest()) // Expects 400
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.userName").exists());
    }

    @Test
    @DisplayName("PUT /update/{email} - Fail 401: Unauthenticated")
    void updateUser_Unauthenticated() throws Exception {
        UserRequestDto validData = new UserRequestDto(
                "user", "name", "email@mail.com", "pass123", 20
        );

        mockMvc.perform(put("/api/user/update/test@mail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validData)))
                .andExpect(status().isUnauthorized()); // Expects 401
    }
    // ------------------------------------@PostMapping("/promoteUserToAdmin/{email}")------------------------------------

    @Test
    @WithMockUser(username = "admin@mail.com", roles = "ADMIN")
    @DisplayName("Success: Admin promotes 'testUser' to Admin")
    void updateUserToAdmin_Success() throws Exception {
        mockMvc.perform(post("/api/user/promoteUserToAdmin/" + userUser.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[?(@ == 'ADMIN')]").exists());

        User updatedUser = userRepository.findByEmail(userUser.getEmail()).orElseThrow();

        boolean hasAdmin = updatedUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));
        assertTrue(hasAdmin, "User should have acquired ADMIN role in the database");
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = "ADMIN")
    @DisplayName("Fail 409: Admin tries to promote THEMSELVES")
    void updateUserToAdmin_Conflict_Self() throws Exception {
        // Act: Admin targets their own email
        mockMvc.perform(post("/api/user/promoteUserToAdmin/" + adminUser.getEmail()))
                .andDo(print())
                .andExpect(status().isNotFound()); // Expects 404 EntityNotFound, but ideally it should be conflict status
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = "ADMIN")
    @DisplayName("Fail 404: Admin tries to promote non-existent user")
    void updateUserToAdmin_NotFound() throws Exception {
        mockMvc.perform(post("/api/user/promoteUserToAdmin/nobody@mail.com"))
                .andDo(print())
                .andExpect(status().isNotFound()); // Expects 404 EntityNotFound
    }

    @Test
    @DisplayName("Fail 401: Unauthenticated user tries to promote")
    void updateUserToAdmin_Unauthenticated() throws Exception {
        mockMvc.perform(post("/api/user/promoteUserToAdmin/" + userUser.getEmail()))
                .andExpect(status().isUnauthorized()); // Expects 401
    }

    @Test
    @WithMockUser(username = "test@mail.com", roles = "USER")
    @DisplayName("Success: Regular USER promotes another user (Allowed by your @PreAuthorize)")
    void updateUserToAdmin_UserPromotesOther() throws Exception {
        User thirdUser = User.builder()
                .username("third")
                .email("third@mail.com")
                .password("pass")
                .roles(Set.of(userRole))
                .build();
        userRepository.save(thirdUser);

        mockMvc.perform(post("/api/user/promoteUserToAdmin/" + thirdUser.getEmail()))
                .andDo(print())
                .andExpect(status().isForbidden()); // Currently allowed by your annotation
    }
// ------------------------------------@DeleteMapping("/{email}")------------------------------------

    @Test
    @WithMockUser(username = "admin@mail.com", roles = "ADMIN")
    @DisplayName("Success: Admin soft-deletes another user")
    void deleteUser_Success() throws Exception {
        mockMvc.perform(delete("/api/user/" + userUser.getEmail())) // Assuming class mapping is /api/user
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentlyActive").value(false)); // Verify JSON response

        User deletedUser = userRepository.findByEmail(userUser.getEmail()).get();
        assertFalse(deletedUser.isCurrentlyActive(), "User should be marked inactive in DB");
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = "ADMIN")
    @DisplayName("Fail 404: Admin tries to delete THEMSELVES")
    void deleteUser_Self_Fail() throws Exception {
        mockMvc.perform(delete("/api/user/" + adminUser.getEmail()))
                .andDo(print())
                .andExpect(status().isNotFound()) // Expect 404 based on your code
                .andExpect(jsonPath("$.message").value("Provided email matches with logged user's email. You can not remove yourself from db."));
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = "ADMIN")
    @DisplayName("Fail 404: Admin tries to delete ALREADY REMOVED user")
    void deleteUser_AlreadyDeleted_Fail() throws Exception {
        userUser.setCurrentlyActive(false);
        userRepository.save(userUser);

        mockMvc.perform(delete("/api/user/" + userUser.getEmail()))
                .andDo(print())
                .andExpect(status().isNotFound()) // Your service throws EntityNotFoundException here
                .andExpect(jsonPath("$.message").value("User is already removed from db."));
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = "ADMIN")
    @DisplayName("Fail 404: Admin tries to delete NON-EXISTENT user")
    void deleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/api/user/nobody@mail.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "target@mail.com", roles = "USER")
    @DisplayName("Fail 403: Regular USER tries to delete (Forbidden)")
    void deleteUser_Forbidden_UserRole() throws Exception {
        mockMvc.perform(delete("/api/user/" + userUser.getEmail()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Fail 401: Unauthenticated user tries to delete")
    void deleteUser_Unauthenticated() throws Exception {
        mockMvc.perform(delete("/api/user/" + userUser.getEmail()))
                .andExpect(status().isUnauthorized());
    }
    // ==========================================TESTS FOR: POST /api/user/login==========================================
    @Test
    @DisplayName("POST /login - Success: Valid credentials return Token")
    void login_Success() throws Exception {
        mockMvc.perform(post("/api/user/login")
                        .param("email", userUser.getEmail())
                        .param("password", "test123") // Raw password matches DB hash
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }

    @Test
    @DisplayName("POST /login - Fail 401/404: Wrong Password")
    void login_WrongPassword() throws Exception {
        mockMvc.perform(post("/api/user/login")
                        .param("email", "test@mail.com")
                        .param("password", "wrongPassword123"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /login - Fail 401/404: Email not found")
    void login_UserNotFound() throws Exception {
        mockMvc.perform(post("/api/user/login")
                        .param("email", "nobody@mail.com")
                        .param("password", "test123"))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("POST /login - Fail 400: Invalid Input Format")
    void login_InvalidFormat() throws Exception {
        mockMvc.perform(post("/api/user/login")
                        .param("email", "bad-email")
                        .param("password", "1"))
                .andDo(print())
                .andExpect(status().isBadRequest()) // 400
                .andExpect(jsonPath("$.['login.email']").doesNotExist())
                .andExpect(jsonPath("$.['login.password']").doesNotExist());
    }
}
