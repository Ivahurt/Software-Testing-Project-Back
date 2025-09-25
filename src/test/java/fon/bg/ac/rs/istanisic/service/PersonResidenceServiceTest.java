package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.converter.PersonResidenceHistoryConverter;
import fon.bg.ac.rs.istanisic.dto.PersonResidenceHistoryDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.repository.PersonResidenceHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonResidenceServiceTest {

    @Mock
    private PersonResidenceHistoryRepository personResidenceHistoryRepository;

    @Mock
    private PersonResidenceHistoryConverter personResidenceHistoryConverter;

    @InjectMocks
    private PersonResidenceService personResidenceService;

    private Person person;
    private City city;
    private PersonResidenceHistory oldHistory;
    private PersonResidenceHistory newHistory;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);
        person.setFirstName("Nikola");
        person.setLastName("Stanisic");

        city = new City();
        city.setId(1L);
        city.setName("Beograd");

        oldHistory = PersonResidenceHistory.builder()
                .person(person)
                .city(city)
                .residenceEnd(null)
                .build();

        newHistory = PersonResidenceHistory.builder()
                .person(person)
                .city(city)
                .residenceEnd(null)
                .build();
    }

    @Test
    @DisplayName("Uspešno čuvanje istorije prebivališta osobe")
    void savePersonResidenceSuccess() throws Exception {
        when(personResidenceHistoryRepository.findByPerson(person)).thenReturn(List.of(oldHistory));
        when(personResidenceHistoryRepository.saveAll(anyList())).thenReturn(List.of(oldHistory));
        when(personResidenceHistoryRepository.save(newHistory)).thenReturn(newHistory);

        personResidenceService.savePersonResidence(newHistory);

        ArgumentCaptor<List<PersonResidenceHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(personResidenceHistoryRepository, times(1)).saveAll(captor.capture());
        List<PersonResidenceHistory> savedList = captor.getValue();

        assertThat(savedList.get(0).getResidenceEnd()).isEqualTo(LocalDate.now());
        verify(personResidenceHistoryRepository, times(1)).save(newHistory);
    }

    @Test
    @DisplayName("Dobavljanje istorije prebivališta osobe kada osoba postoji")
    void getPersonResidenceHistorySuccess() throws Exception {
        oldHistory.setResidenceEnd(LocalDate.of(2023, 1, 1));
        oldHistory.setPerson(person);
        oldHistory.setCity(city);

        when(personResidenceHistoryRepository.findByPersonUniqueIdentificationNumber(person.getId()))
                .thenReturn(List.of(oldHistory));

        PersonResidenceHistoryDTO dto = new PersonResidenceHistoryDTO(
                person.getFirstName(),
                person.getLastName(),
                city.getName(),
                LocalDate.now().minusYears(3),
                oldHistory.getResidenceEnd()
        );

        when(personResidenceHistoryConverter.listToDTO(List.of(oldHistory))).thenReturn(List.of(dto));

        List<PersonResidenceHistoryDTO> result = personResidenceService.getPersonResidenceHistory(person.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).firstName()).isEqualTo(person.getFirstName());
        assertThat(result.get(0).lastName()).isEqualTo(person.getLastName());
        assertThat(result.get(0).cityResidenceName()).isEqualTo(city.getName());
    }

    @Test
    @DisplayName("Dobavljanje istorije prebivališta osobe kada osoba ne postoji")
    void getPersonResidenceHistoryPersonNotFound() {
        when(personResidenceHistoryRepository.findByPersonUniqueIdentificationNumber(person.getId()))
                .thenReturn(List.of());

        assertThatThrownBy(() -> personResidenceService.getPersonResidenceHistory(person.getId()))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Ne postoji osoba sa jmbg-om: " + person.getId());
    }
}