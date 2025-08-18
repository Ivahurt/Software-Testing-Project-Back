package fon.bg.ac.rs.istanisic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import fon.bg.ac.rs.istanisic.model.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long>{
    Optional<Person> findByUniqueIdentificationNumber(Long id);
    void deleteByUniqueIdentificationNumber(Long id);
}
