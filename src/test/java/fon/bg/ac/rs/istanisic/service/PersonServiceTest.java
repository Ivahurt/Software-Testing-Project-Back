package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.converter.PersonConverter;
import fon.bg.ac.rs.istanisic.dto.PersonDTO;
import fon.bg.ac.rs.istanisic.dto.PersonUpdateDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.repository.CityRepository;
import fon.bg.ac.rs.istanisic.repository.PersonRepository;
import fon.bg.ac.rs.istanisic.repository.foreign_key.PersonResidenceHistoryId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonConverter personConverter;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private PersonResidenceService personResidenceService;

    @InjectMocks
    private PersonService personService;

    @Test
    @DisplayName("Get all persons - empty list")
    void getAllPersonsEmpty() {
        when(personRepository.findAll()).thenReturn(List.of());
        when(personConverter.listToDTO(List.of())).thenReturn(List.of());

        var result = personService.getAll();

        assertThat(result).isEmpty();
        verify(personRepository, times(1)).findAll();
        verify(personConverter, times(1)).listToDTO(List.of());
    }

    @Test
    @DisplayName("Save person - valid data")
    void savePersonValid() throws Exception {
        PersonDTO dto = new PersonDTO(1L, "Pera", "Peric",
                LocalDate.of(1995, 5, 15),
                111222222L,
                "Novi Sad", "Beograd");

        Person person = new Person();
        City cityBirth = new City(1L, 11000, "Novi Sad", 200000);
        City cityResidence = new City(2L, 12000, "Beograd", 1000000);

        when(personConverter.toEntity(dto)).thenReturn(person);
        when(cityRepository.findByName("Novi Sad")).thenReturn(Optional.of(cityBirth));
        when(cityRepository.findByName("Beograd")).thenReturn(Optional.of(cityResidence));
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(personConverter.toDto(person)).thenReturn(dto);

        var result = personService.savePerson(dto);

        assertThat(result).isEqualTo(dto);
        verify(personConverter).toEntity(dto);
        verify(cityRepository).findByName("Novi Sad");
        verify(cityRepository).findByName("Beograd");
        verify(personRepository).save(person);
        verify(personConverter).toDto(person);
    }

    @Test
    @DisplayName("Save person throws exception if city of birth missing")
    void savePersonCityOfBirthMissing() {
        PersonDTO personDTO = new PersonDTO(
                1L, "Nikola", "Stanisic",
                LocalDate.of(1995, 5, 15),
                12345L,
                "Nepostojeci Grad",
                "Novi Sad"
        );

        Person personEntity = new Person();
        when(personConverter.toEntity(personDTO)).thenReturn(personEntity);
        when(cityRepository.findByName("Nepostojeci Grad")).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> personService.savePerson(personDTO));
        assertThat(ex.getMessage()).isEqualTo("Mesto rođenja ne može da bude prazno!");
        verify(cityRepository, times(1)).findByName("Nepostojeci Grad");
        verify(personRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete person successfully")
    void deletePersonValid() throws Exception {
        Long id = 12345L;
        Person foundPerson = new Person();

        when(personRepository.findByUniqueIdentificationNumber(id)).thenReturn(Optional.of(foundPerson));

        personService.deletePerson(id);

        verify(personRepository, times(1)).findByUniqueIdentificationNumber(id);
        verify(personRepository, times(1)).deleteByUniqueIdentificationNumber(id);
    }

    @Test
    @DisplayName("Delete person throws exception if id is null")
    void deletePersonIdNull() {
        assertThatThrownBy(() -> personService.deletePerson(null))
                .isInstanceOf(Exception.class)
                .hasMessage("Id osobe mora da bude unet");

        verify(personRepository, never()).findByUniqueIdentificationNumber(any());
        verify(personRepository, never()).deleteByUniqueIdentificationNumber(any());
    }

    @Test
    @DisplayName("Delete person throws exception if person not found")
    void deletePersonNotFound() {
        Long id = 999L;

        when(personRepository.findByUniqueIdentificationNumber(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personService.deletePerson(id))
                .isInstanceOf(Exception.class)
                .hasMessage("Osoba ne postoji");

        verify(personRepository, times(1)).findByUniqueIdentificationNumber(id);
        verify(personRepository, never()).deleteByUniqueIdentificationNumber(any());
    }

    @Test
    @DisplayName("Update person - valid")
    void updatePersonValid() throws Exception {
        PersonUpdateDTO updateDto = new PersonUpdateDTO(1L, "Beograd");
        Person person = new Person();
        person.setId(1L);
        City city = new City(2L, 11000, "Beograd", 1000000);

        when(personRepository.findByUniqueIdentificationNumber(1L)).thenReturn(Optional.of(person));
        when(cityRepository.findByName("Beograd")).thenReturn(Optional.of(city));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        PersonDTO expectedDto = new PersonDTO(1L, "Pera", "Peric",
                LocalDate.of(1995, 5, 15),
                111222222L,
                "Novi Sad", "Beograd");

        when(personConverter.toDto(person)).thenReturn(expectedDto);

        var result = personService.updatePerson(updateDto);

        assertThat(result).isEqualTo(expectedDto);
        verify(personResidenceService).savePersonResidence(any(PersonResidenceHistory.class));
        verify(personRepository).save(person);
    }

    @Test
    @DisplayName("Update person - not found")
    void updatePersonNotFound() {
        PersonUpdateDTO updateDto = new PersonUpdateDTO(1L, "Beograd");
        when(personRepository.findByUniqueIdentificationNumber(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personService.updatePerson(updateDto))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Osoba ne postoji");
    }
}