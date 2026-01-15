package com.edu.film_database.controller;

import com.edu.film_database.config.JwtFilter;
import com.edu.film_database.config.JwtUtil;
import com.edu.film_database.config.SecurityConfig;
import com.edu.film_database.dto.response.UserResponseDto;
import com.edu.film_database.model.Role;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.RoleRepository;
import com.edu.film_database.repo.UserRepository;
import com.edu.film_database.service.CustomUserDetailsService;
import com.edu.film_database.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(properties = "spring.security.enabled=false")
@AutoConfigureMockMvc(addFilters = false)
public class UserControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @MockitoBean
    private UserRepository userRepository;

    //Test data
    private Role userRole;
    private Role adminRole;
    private User userUser;
    private User adminUser;

    @BeforeEach
    public void setUp() {
        roleRepository.deleteAll();
        userRepository.deleteAll();

        userRole = Role.builder().name("USER").build();
        roleRepository.save(userRole);
        adminRole = Role.builder().name("ADMIN").build();
        roleRepository.save(adminRole);

        userUser = User.builder().username("testUser").fullName("testUserFullName").email("testUser@somedomain.com").password("test123").currentlyActive(true).roles(Set.of(userRole)).build();
        userRepository.save(userUser);

        adminUser = User.builder().username("admin").fullName("admin").email("admin@exe.com").password("admin").currentlyActive(true).roles(Set.of(adminRole)).build();
        userRepository.save(adminUser);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        adminUser.getEmail(), null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

    }


    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Verify that UserController follows UserApi routing and security")
    void testGetAllUsers_AdminSuccess() throws Exception {
        mockMvc.perform(get("/api/user/get/list"))
                .andExpect(status().isOk());
    }
}
