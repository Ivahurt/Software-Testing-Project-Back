package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.converter.PaymentConverter;
import fon.bg.ac.rs.istanisic.converter.PersonConverter;
import fon.bg.ac.rs.istanisic.dto.PaymentDTO;
import fon.bg.ac.rs.istanisic.dto.PersonDTO;
import fon.bg.ac.rs.istanisic.dto.PersonUpdateDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.repository.CityRepository;
import fon.bg.ac.rs.istanisic.repository.PaymentRepository;
import fon.bg.ac.rs.istanisic.repository.PersonRepository;
import fon.bg.ac.rs.istanisic.repository.foreign_key.PersonResidenceHistoryId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonConverter personConverter;
    private final CityRepository cityRepository;

    private final PersonResidenceService personResidenceService;

    private final PaymentConverter paymentConverter;
    private final PaymentRepository paymentRepository;


    public List<PersonDTO> getAll() {
        return personConverter.listToDTO(personRepository.findAll());
    }

    public PersonDTO savePerson(PersonDTO personDTO) throws Exception {
        Person person = personConverter.toEntity(personDTO);
        Optional<City> cityOfBirth = cityRepository.findByName(personDTO.cityBirthName());
        Optional<City> cityOfResidence = cityRepository.findByName(personDTO.cityResidenceName());

        if (cityOfBirth.isEmpty()) {
            throw new Exception("Mesto rođenja ne može da bude prazno!");
        }

        if (cityOfResidence.isEmpty()) {
            throw new Exception("Mesto stanovanja ne može da bude prazno");
        }

        person.setCityOfBirth(cityOfBirth.get());
        person.setCityOfResidence(cityOfResidence.get());

        person = personRepository.save(person);

        PersonResidenceHistoryId prhId = new PersonResidenceHistoryId(
                person.getId(),
                person.getCityOfResidence().getId(),
                LocalDate.now()
        );

        PersonResidenceHistory personResidenceHistory = new PersonResidenceHistory();
        personResidenceHistory.setPersonResidenceHistoryId(prhId);
        personResidenceHistory.setPerson(person);
        personResidenceHistory.setCity(person.getCityOfResidence());
        personResidenceHistory.setResidenceEnd(null);

        personResidenceService.savePersonResidence(personResidenceHistory);

        return personConverter.toDto(person);
    }

    @Transactional
    public void deletePerson(Long id) throws Exception{
        if (id == null) {
            throw new Exception("Id osobe mora da bude unet");
        }
        if (personRepository.findByUniqueIdentificationNumber(id).isEmpty()) {
            throw new Exception("Osoba ne postoji");

        }
        personRepository.deleteByUniqueIdentificationNumber(id);
    }

    public PersonDTO updatePerson(PersonUpdateDTO personDTO) throws Exception{
        Long uniqueIdentificationNumber = personDTO.uniqueIdentificationNumber();
        Optional<Person> foundPerson = personRepository.findByUniqueIdentificationNumber(uniqueIdentificationNumber);
        if (foundPerson.isEmpty()) {
            throw new Exception("Osoba ne postoji");
        }

        Optional<City> foundCity = cityRepository.findByName(personDTO.cityResidenceName());
        if (foundCity.isEmpty()) {
            throw new Exception("Mesto ne postoji");
        }

        Person updatedPerson = foundPerson.get();
        updatedPerson.setCityOfResidence(foundCity.get());

        PersonResidenceHistoryId prhId = new PersonResidenceHistoryId(
                foundPerson.get().getId(),
                foundCity.get().getId(),
                LocalDate.now()
        );

        PersonResidenceHistory personResidenceHistory = new PersonResidenceHistory();
        personResidenceHistory.setPersonResidenceHistoryId(prhId);
        personResidenceHistory.setPerson(foundPerson.get());
        personResidenceHistory.setCity(foundCity.get());
        personResidenceHistory.setResidenceEnd(null);
        personResidenceService.savePersonResidence(personResidenceHistory);

        return personConverter.toDto(personRepository.save(updatedPerson));
    }

    public PersonDTO getPersonAgeInMonths(Long uniqueId) throws Exception{
        Optional<Person> foundPerson = personRepository.findByUniqueIdentificationNumber(uniqueId);
        if (foundPerson.isEmpty()) {
            throw new Exception("Osoba ne postoji");
        }

        Person person = foundPerson.get();
        LocalDate today = LocalDate.now();
        Integer ageInMonths1 = (int) ChronoUnit.MONTHS.between(person.getDateOfBirth(), today);

        person.setAgeInMonths1(ageInMonths1);
        return personConverter.toDto(person);
    }

    public PersonDTO getPersonSumPayments(Long uniqueId) throws Exception{
        Optional<Person> foundPerson = personRepository.findByUniqueIdentificationNumber(uniqueId);
        if (foundPerson.isEmpty()) {
            throw new Exception("Osoba ne postoji");
        }

        Person person = foundPerson.get();
        System.out.println("Pronadjena je osoba za ispis svih njenih isplata");
        System.out.println(person);

        return personConverter.toDto(person);
    }
}
