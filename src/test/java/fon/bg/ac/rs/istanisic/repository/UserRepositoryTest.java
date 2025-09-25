package fon.bg.ac.rs.istanisic.repository;

import fon.bg.ac.rs.istanisic.model.User;
import fon.bg.ac.rs.istanisic.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final User exampleUser = User.builder()
            .firstName("Nikola")
            .lastName("Istanisic")
            .username("nikola123")
            .password("password")
            .role(UserType.Administrator)
            .build();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(exampleUser);
    }

    @Test
    void findByIdFound() {
        Optional<User> found = userRepository.findById(exampleUser.getId());
        assertThat(found).isPresent().contains(exampleUser);
    }

    @Test
    void findByIdNotFound() {
        Optional<User> found = userRepository.findById(-1L);
        assertThat(found).isEmpty();
    }

    @Test
    void findByRoleFound() {
        List<User> admins = userRepository.findByRole(UserType.Administrator);
        assertThat(admins).hasSize(1).contains(exampleUser);
    }

    @Test
    void findByRoleNotFound() {
        List<User> users = userRepository.findByRole(UserType.Korisnik);
        assertThat(users).isEmpty();
    }

    @Test
    void deleteUserById() {
        userRepository.deleteById(exampleUser.getId());
        Optional<User> found = userRepository.findById(exampleUser.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void findAdminUser() {
        List<User> admins = userRepository.findByRole(UserType.Administrator);

        assertThat(admins)
                .hasSize(1)
                .contains(exampleUser);
    }
}