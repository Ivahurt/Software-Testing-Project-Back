package fon.bg.ac.rs.istanisic.converter;

import fon.bg.ac.rs.istanisic.dto.PersonResidenceHistoryDTO;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import org.springframework.stereotype.Component;

@Component
public class PersonResidenceHistoryConverter implements DTOEntityConverter<PersonResidenceHistoryDTO,
        PersonResidenceHistory>{

    @Override
    public PersonResidenceHistory toEntity(PersonResidenceHistoryDTO personResidenceHistoryDTO) {
        return null;
    }

    @Override
    public PersonResidenceHistoryDTO toDto(PersonResidenceHistory personResidenceHistory) {
        return personResidenceHistory == null ? null : new PersonResidenceHistoryDTO(
                personResidenceHistory.getPerson().getFirstName(),
                personResidenceHistory.getPerson().getLastName(),
                personResidenceHistory.getCity().getName(),
                personResidenceHistory.getPersonResidenceHistoryId().getResidenceStart(),
                personResidenceHistory.getResidenceEnd()
        );
    }
}


