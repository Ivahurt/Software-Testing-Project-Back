package fon.bg.ac.rs.istanisic.converter;

import fon.bg.ac.rs.istanisic.dto.PersonResidenceHistoryDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.repository.foreign_key.PersonResidenceHistoryId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class PersonResidenceHistoryConverterTest {

    private final PersonResidenceHistoryConverter converter = new PersonResidenceHistoryConverter();

    @Test
    void testToDtoNotNull() {
        Person person = Person.builder()
                .id(1L)
                .firstName("Nikola")
                .lastName("Istanisic")
                .build();

        City city = City.builder()
                .id(2L)
                .name("Beograd")
                .postalCode(11000)
                .population(1500000)
                .build();

        PersonResidenceHistoryId id = new PersonResidenceHistoryId(
                person.getId(),
                city.getId(),
                LocalDate.of(2020, 1, 1)
        );

        PersonResidenceHistory history = PersonResidenceHistory.builder()
                .person(person)
                .city(city)
                .personResidenceHistoryId(id)
                .residenceEnd(LocalDate.of(2022, 1, 1))
                .build();

        PersonResidenceHistoryDTO dto = converter.toDto(history);

        assertThat(dto).isNotNull();
        assertThat(dto.firstName()).isEqualTo("Nikola");
        assertThat(dto.lastName()).isEqualTo("Istanisic");
        assertThat(dto.cityResidenceName()).isEqualTo("Beograd");
        assertThat(dto.residenceStart()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(dto.residenceEnd()).isEqualTo(LocalDate.of(2022, 1, 1));
    }

    @Test
    void testToDtoNull() {
        assertNull(converter.toDto(null));
    }

    @Test
    void testToEntityReturnsNull() {
        PersonResidenceHistoryDTO dto = new PersonResidenceHistoryDTO(
                "Nikola", "Istanisic", "Beograd",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2022, 1, 1)
        );

        assertNull(converter.toEntity(dto));
    }
}