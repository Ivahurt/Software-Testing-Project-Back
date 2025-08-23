package fon.bg.ac.rs.istanisic.repository;

import fon.bg.ac.rs.istanisic.SoftwareTestingApplication;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.Person;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PersonRepository personRepository;

    private final City exampleCity = new City(null, 15000, "Bor", 20000);

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
        cityRepository.deleteAll();

        cityRepository.save(exampleCity);
    }

    @Test
    void findByPostalCodeFound() {
        Optional<City> found = cityRepository.findByPostalCode(15000);
        assertThat(found).isPresent().contains(exampleCity);
    }

    @Test
    void findByPostalCodeNotFound() {
        Optional<City> found = cityRepository.findByPostalCode(11000);
        assertThat(found).isEmpty();
    }

    @Test
    void findByNameFound() {
        Optional<City> found = cityRepository.findByName("Bor");
        assertThat(found).isPresent().contains(exampleCity);
    }

    @Test
    void findByNameNotFound() {
        Optional<City> found = cityRepository.findByName("Novi Sad");
        assertThat(found).isEmpty();
    }

    @Test
    void deleteByPostalCode() {
        cityRepository.deleteByPostalCode(15000);
        assertThat(cityRepository.findByPostalCode(15000)).isEmpty();
    }
}