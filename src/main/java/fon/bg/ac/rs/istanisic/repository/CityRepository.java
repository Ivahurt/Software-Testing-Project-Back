package fon.bg.ac.rs.istanisic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fon.bg.ac.rs.istanisic.model.City;

import java.util.Optional;

public interface CityRepository  extends  JpaRepository<City, Long>{
    Optional<City> findByPostalCode(int i);
}
