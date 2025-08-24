package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.converter.PersonResidenceHistoryConverter;
import fon.bg.ac.rs.istanisic.dto.CityDTO;
import fon.bg.ac.rs.istanisic.dto.PersonDTO;
import fon.bg.ac.rs.istanisic.dto.PersonResidenceHistoryDTO;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.repository.PersonResidenceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonResidenceService {
    private final PersonResidenceHistoryRepository personResidenceHistoryRepository;
    private final PersonResidenceHistoryConverter personResidenceHistoryConverter;

    public void savePersonResidence(PersonResidenceHistory personResidenceHistory) throws Exception {
        List<PersonResidenceHistory> personResidenceHistories = personResidenceHistoryRepository.findByPerson(personResidenceHistory.getPerson());
        LocalDate today = LocalDate.now();

        for (PersonResidenceHistory history : personResidenceHistories) {
            if (history.getResidenceEnd() == null || history.getResidenceEnd().isAfter(today)) {
                history.setResidenceEnd(today);
            }
        }

        personResidenceHistoryRepository.saveAll(personResidenceHistories);
        personResidenceHistoryRepository.save(personResidenceHistory);

    }

    public List<PersonResidenceHistoryDTO> getPersonResidenceHistory(Long uniqueId) throws Exception {
        List<PersonResidenceHistory> residenceHistories = personResidenceHistoryRepository.findByPersonUniqueIdentificationNumber(uniqueId);
        if (residenceHistories.isEmpty()) {
            throw new Exception("Ne postoji osoba sa jmbg-om: "+ uniqueId);
        }
        return personResidenceHistoryConverter.listToDTO(residenceHistories);
    }

}