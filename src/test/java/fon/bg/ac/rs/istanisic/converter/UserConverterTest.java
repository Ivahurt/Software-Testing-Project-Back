package fon.bg.ac.rs.istanisic.converter;

import fon.bg.ac.rs.istanisic.dto.UserDTO;
import fon.bg.ac.rs.istanisic.model.User;
import fon.bg.ac.rs.istanisic.model.UserType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserConverterTest {

    private final UserConverter converter = new UserConverter();

    @Test
    void testToEntityNotNull() {
        UserDTO dto = new UserDTO(
                "Nikola",
                "Istanisic",
                "nikola123",
                "securePass",
                UserType.Administrator
        );

        User entity = converter.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getFirstName()).isEqualTo("Nikola");
        assertThat(entity.getLastName()).isEqualTo("Istanisic");
        assertThat(entity.getUsername()).isEqualTo("nikola123");
        assertThat(entity.getPassword()).isEqualTo("securePass");
        assertThat(entity.getRole()).isEqualTo(UserType.Administrator);
    }

    @Test
    void testToEntityNull() {
        assertNull(converter.toEntity(null));
    }

    @Test
    void testToDtoNotNull() {
        User user = User.builder()
                .firstName("Petar")
                .lastName("Petrovic")
                .username("pera")
                .password("peraPass")
                .role(UserType.Korisnik)
                .build();

        UserDTO dto = converter.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.firstName()).isEqualTo("Petar");
        assertThat(dto.lastName()).isEqualTo("Petrovic");
        assertThat(dto.username()).isEqualTo("pera");
        assertThat(dto.password()).isEqualTo("peraPass");
        assertThat(dto.role()).isEqualTo(UserType.Korisnik);
    }

    @Test
    void testToDtoNull() {
        assertNull(converter.toDto(null));
    }
}