package com.edu.film_database.repository.userRepository;

import com.edu.film_database.model.Role;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.RoleRepository;
import com.edu.film_database.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {
    @Autowired private UserRepository userRepository;

    @Autowired RoleRepository roleRepository;

    private User user;
    private Role role;

    @BeforeEach
    public void setup() {
        role = Role.builder().name("USER").build();
        roleRepository.save(role);
        user = User.builder()
                .username("testUser")
                .fullName("testUser")
                .password("password")
                .email("test@email.com")
                .age(30)
                .currentlyActive(true)
                .roles(Set.of(role))
                .build();
        userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        role = null;
        user = null;
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("findByEmail with matching user present, should return Optional of User")
    public void findByEmailPresent(){
        Optional<User> user_tmp = userRepository.findByEmail(user.getEmail());

        assertTrue(user_tmp.isPresent());
        assertEquals(user.getEmail(), user_tmp.get().getEmail());
    }

    @Test
    @DisplayName("findByEmail with no matching user present, should return object of empty Optional")
    public void findByEmailEmpty(){
        Optional<User> user_tmp = userRepository.findByEmail("notUser");

        assertTrue(user_tmp.isEmpty());
    }

}
