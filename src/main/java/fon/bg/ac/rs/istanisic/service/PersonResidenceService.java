package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.dto.CityDTO;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.repository.PersonResidenceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonResidenceService {
    private final PersonResidenceHistoryRepository personResidenceHistoryRepository;

    public void savePersonResidence(PersonResidenceHistory personResidenceHistory) throws Exception{
        List<PersonResidenceHistory>  personResidenceHistories = personResidenceHistoryRepository.findByPerson(personResidenceHistory.getPerson());
        LocalDate today = LocalDate.now();

        for (PersonResidenceHistory history : personResidenceHistories) {
            if (history.getResidenceEnd() == null || history.getResidenceEnd().isAfter(today)) {
                history.setResidenceEnd(today);
            }
        }

        personResidenceHistoryRepository.saveAll(personResidenceHistories);
        personResidenceHistoryRepository.save(personResidenceHistory);

    }
}
