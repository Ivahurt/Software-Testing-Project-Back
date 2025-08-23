package fon.bg.ac.rs.istanisic.repository;

import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.repository.foreign_key.PersonResidenceHistoryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonResidenceHistoryRepositoryTest {

    @Autowired
    private PersonResidenceHistoryRepository historyRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CityRepository cityRepository;

    private Person examplePerson;
    private City exampleCity;
    private PersonResidenceHistory exampleHistory;

    @BeforeEach
    void setUp() {
        historyRepository.deleteAll();
        personRepository.deleteAll();
        cityRepository.deleteAll();

        int postalCode = 11000 + (int) (Math.random() * 10000); // validan i unikatan

        exampleCity = cityRepository.save(new City(null, postalCode, "Beograd", 1370000));

        examplePerson = personRepository.save(Person.builder()
                .firstName("Marko")
                .lastName("Markovic")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .uniqueIdentificationNumber(123456789L)
                .cityOfBirth(exampleCity)
                .cityOfResidence(exampleCity)
                .build());

        PersonResidenceHistoryId historyId = new PersonResidenceHistoryId(
                examplePerson.getId(),
                exampleCity.getId(),
                LocalDate.of(2020, 1, 1)
        );

        exampleHistory = PersonResidenceHistory.builder()
                .personResidenceHistoryId(historyId)
                .person(examplePerson)
                .city(exampleCity)
                .residenceEnd(LocalDate.of(2021, 1, 1))
                .build();

        historyRepository.save(exampleHistory);
    }

    @Test
    void findByPersonFound() {
        List<PersonResidenceHistory> found = historyRepository.findByPerson(examplePerson);
        assertThat(found).hasSize(1).contains(exampleHistory);
    }

    @Test
    void findByPersonNotFound() {
        int postalCode = 11000 + (int) (Math.random() * 10000);
        City newCity = cityRepository.save(new City(null, postalCode, "Novi Beograd", 250000));

        Person newPerson = personRepository.save(Person.builder()
                .firstName("Jovan")
                .lastName("Jovanovic")
                .dateOfBirth(LocalDate.of(1990, 3, 10))
                .uniqueIdentificationNumber(987654321L)
                .cityOfBirth(newCity)
                .cityOfResidence(newCity)
                .build());

        List<PersonResidenceHistory> found = historyRepository.findByPerson(newPerson);
        assertThat(found).isEmpty();
    }

    @Test
    void deleteHistory() {
        historyRepository.delete(exampleHistory);
        List<PersonResidenceHistory> found = historyRepository.findByPerson(examplePerson);
        assertThat(found).isEmpty();
    }
}