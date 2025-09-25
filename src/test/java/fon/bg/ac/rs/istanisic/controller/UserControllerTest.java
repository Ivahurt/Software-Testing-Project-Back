package fon.bg.ac.rs.istanisic.controller;

import fon.bg.ac.rs.istanisic.dto.UserDTO;
import fon.bg.ac.rs.istanisic.model.User;
import fon.bg.ac.rs.istanisic.model.UserType;
import fon.bg.ac.rs.istanisic.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstName("Iva")
                .lastName("Istanisic")
                .username("iva123")
                .password("password")
                .role(UserType.Administrator)
                .build();

        userDTO = new UserDTO("Iva", "Istanisic", "iva123", "password", UserType.Administrator);
    }

    @Test
    @DisplayName("Vraćanje svih korisnika")
    void getAllUsersTest() throws Exception {
        when(userService.getAll()).thenReturn(List.of(user));

        ResponseEntity<List<User>> response = userController.getAll();

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1).contains(user);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        verify(userService, times(1)).getAll();
    }

    @Test
    @DisplayName("Vraćanje svih korisnika sa ulogom korisnik")
    void getAllUsersByRoleTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1).contains(user);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Čuvanje novog korisnika")
    void saveUserTest() throws Exception {
        doNothing().when(userService).saveUser(userDTO);

        ResponseEntity<Void> response = userController.saveUser(userDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(userService, times(1)).saveUser(userDTO);
    }
}