package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.converter.PersonConverter;
import fon.bg.ac.rs.istanisic.dto.PersonDTO;
import fon.bg.ac.rs.istanisic.dto.PersonUpdateDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.repository.CityRepository;
import fon.bg.ac.rs.istanisic.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonConverter personConverter;
    private final CityRepository cityRepository;

    public List<PersonDTO> getAll() {
        return personConverter.listToDTO(personRepository.findAll());
    }

    public PersonDTO savePerson(PersonDTO personDTO) throws Exception {
        Person person = personConverter.toEntity(personDTO);
        Optional<City> cityOfBirth = cityRepository.findByName(personDTO.cityBirthName());
        Optional<City> cityOfResidence = cityRepository.findByName(personDTO.cityResidenceName());
        if (cityOfBirth.isEmpty()) {
            throw new Exception("City of birth is empty!");
        }

        if (cityOfResidence.isEmpty()) {
            throw new Exception("City of residence is empty");
        }

        person.setCityOfBirth(cityOfBirth.get());
        person.setCityOfResidence(cityOfResidence.get());
        return personConverter.toDto(personRepository.save(person));

    }

    @Transactional
    public void deletePerson(Long id) throws Exception{
        if (id == null) {
            throw new Exception("Person id can not be null");
        }
        if (personRepository.findByUniqueIdentificationNumber(id).isEmpty()) {
            throw new Exception("Person doesn't exist");

        }
        personRepository.deleteByUniqueIdentificationNumber(id);
    }

    public PersonDTO updatePerson(PersonUpdateDTO personDTO) throws Exception{
        Long uniqueIdentificationNumber = personDTO.uniqueIdentificationNumber();
        Optional<Person> foundPerson = personRepository.findByUniqueIdentificationNumber(uniqueIdentificationNumber);
        if (foundPerson.isEmpty()) {
            throw new Exception("Person doesn't exist");
        }

        Optional<City> foundCity = cityRepository.findByName(personDTO.cityResidenceName());
        if (foundCity.isEmpty()) {
            throw new Exception("City doesn't exist");
        }

        Person updatedPerson = foundPerson.get();
        updatedPerson.setCityOfResidence(foundCity.get());
        return personConverter.toDto(personRepository.save(updatedPerson));
    }
}
