package fon.bg.ac.rs.istanisic.repository;

import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.repository.foreign_key.PersonResidenceHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonResidenceHistoryRepository extends JpaRepository<PersonResidenceHistory, PersonResidenceHistoryId> {
    List<PersonResidenceHistory> findByPersonUniqueIdentificationNumber(Long uniqueId);
    List<PersonResidenceHistory> findByPerson(Person person);
}
