package fon.bg.ac.rs.istanisic.repository;

import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.City;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CityRepository cityRepository;

    private City exampleCity;
    private Person examplePerson;

    @BeforeEach
    void setUp() {
        exampleCity = cityRepository.save(new City(null, 15000, "Bor", 20000));

        examplePerson = new Person();
        examplePerson.setUniqueIdentificationNumber(123456789L);
        examplePerson.setFirstName("Nikola");
        examplePerson.setLastName("Istanisic");
        examplePerson.setDateOfBirth(LocalDate.of(1995, 5, 20));
        examplePerson.setCityOfBirth(exampleCity);

        personRepository.save(examplePerson);
    }

    @Test
    void findByUniqueIdentificationNumberFound() {
        Optional<Person> found = personRepository.findByUniqueIdentificationNumber(123456789L);
        assertThat(found).isPresent().contains(examplePerson);
    }

    @Test
    void findByUniqueIdentificationNumberNotFound() {
        Optional<Person> found = personRepository.findByUniqueIdentificationNumber(987654321L);
        assertThat(found).isEmpty();
    }

    @Test
    void deleteByUniqueIdentificationNumber() {
        personRepository.deleteByUniqueIdentificationNumber(123456789L);
        assertThat(personRepository.findByUniqueIdentificationNumber(123456789L)).isEmpty();
    }
}