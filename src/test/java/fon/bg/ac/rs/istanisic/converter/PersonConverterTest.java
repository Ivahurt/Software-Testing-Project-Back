package fon.bg.ac.rs.istanisic.converter;

import fon.bg.ac.rs.istanisic.dto.PersonDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonConverterTest {
    private final PersonConverter personConverter = new PersonConverter();

    @Test
    void testToEntityNotNull() {
        PersonDTO dto = new PersonDTO(
                1L,
                "Pera",
                "Peric",
                LocalDate.of(2000, 3, 4),
                100, // ageInMonths1
                10090L,
                2888028903L,
                "Beograd",
                "Beograd"
        );

        Person entity = personConverter.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getFirstName()).isEqualTo("Pera");
        assertThat(entity.getLastName()).isEqualTo("Peric");
        assertThat(entity.getUniqueIdentificationNumber()).isEqualTo(2888028903L);
        assertThat(entity.getCityOfBirth().getName()).isEqualTo("Beograd");
        assertThat(entity.getCityOfResidence().getName()).isEqualTo("Beograd");
    }

    @Test
    public void testToDtoNotNull() {
        City city = City.builder()
                .postalCode(11000)
                .name("Beograd")
                .population(1300000)
                .build();

        Person person = Person.builder()
                .id(2L)
                .firstName("Pera")
                .lastName("Peric")
                .dateOfBirth(LocalDate.of(2000, 3, 4))
                .ageInMonths(100)
                .uniqueIdentificationNumber(999229939L)
                .cityOfBirth(city)
                .cityOfResidence(city)
                .build();

        PersonDTO dto = personConverter.toDto(person);

        assertNotNull(dto);
        assertEquals(person.getId(), dto.id());
        assertEquals(person.getFirstName(), dto.firstName());
        assertEquals(person.getLastName(), dto.lastName());
        assertEquals(person.getDateOfBirth(), dto.dateOfBirth());
        assertEquals(person.getUniqueIdentificationNumber(), dto.uniqueIdentificationNumber());
        assertEquals(person.getCityOfBirth().getName(), dto.cityBirthName());
        assertEquals(person.getCityOfResidence().getName(), dto.cityResidenceName());
    }

    @Test
    public void testToEntityNull() {
        assertNull(personConverter.toEntity(null));
    }

    @Test
    public void testToDtoNull() {
        assertNull(personConverter.toDto(null));
    }
}