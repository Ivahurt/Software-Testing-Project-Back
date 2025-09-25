package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.converter.UserConverter;
import fon.bg.ac.rs.istanisic.dto.UserDTO;
import fon.bg.ac.rs.istanisic.model.User;
import fon.bg.ac.rs.istanisic.model.UserType;
import fon.bg.ac.rs.istanisic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserService userService;

    private User adminUser;
    private UserDTO adminUserDTO;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .firstName("Iva")
                .lastName("Istanisic")
                .username("iva123")
                .password("password")
                .role(UserType.Administrator)
                .build();

        adminUserDTO = new UserDTO("Iva", "Istanisic",
                "iva123", "password", UserType.Administrator);
    }

    @Test
    @DisplayName("Get all users")
    void getAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(adminUser));

        var result = userService.getAll();

        assertThat(result).hasSize(1).contains(adminUser);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get all users with role KORISNIK")
    void getAllUsersByRole() throws Exception {
        User user = User.builder()
                .firstName("Petar")
                .lastName("Petrovic")
                .username("petar123")
                .password("pass")
                .role(UserType.Korisnik)
                .build();

        when(userRepository.findByRole(UserType.Korisnik)).thenReturn(List.of(user));

        var result = userService.getAllUsers();

        assertThat(result).hasSize(1).contains(user);
        verify(userRepository, times(1)).findByRole(UserType.Korisnik);
    }

    @Test
    @DisplayName("Save user")
    void saveUser() throws Exception {
        when(userConverter.toEntity(adminUserDTO)).thenReturn(adminUser);

        userService.saveUser(adminUserDTO);

        verify(userConverter, times(1)).toEntity(adminUserDTO);
        verify(userRepository, times(1)).save(adminUser);
    }
}
