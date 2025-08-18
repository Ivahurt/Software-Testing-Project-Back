package fon.bg.ac.rs.istanisic.converter;

import fon.bg.ac.rs.istanisic.dto.PersonDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonConverter implements DTOEntityConverter<PersonDTO, Person>{
    @Override
    public Person toEntity(PersonDTO personDTO) {
        return personDTO == null ? null :
                Person.builder()
                    .id(personDTO.id())
                    .firstName(personDTO.firstName())
                    .lastName(personDTO.lastName())
                    .dateOfBirth(personDTO.dateOfBirth())
                        .uniqueIdentificationNumber(personDTO.uniqueIdentificationNumber())
                    .cityOfBirth(City.builder()
                            .name(personDTO.cityBirthName())
                            .build())
                    .cityOfResidence(City.builder()
                            .name(personDTO.cityResidenceName())
                            .build())
                    .build();
    }

    @Override
    public PersonDTO toDto(Person person) {
        return person == null ? null : new PersonDTO(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getDateOfBirth(),
                person.getUniqueIdentificationNumber(),
                person.getCityOfBirth().getName(),
                person.getCityOfResidence().getName()

        );
    }
}
