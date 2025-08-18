package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.controller.CityController;
import fon.bg.ac.rs.istanisic.converter.CityConverter;
import fon.bg.ac.rs.istanisic.dto.CityDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.repository.CityRepository;
import fon.bg.ac.rs.istanisic.repository.PersonRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final CityConverter cityConverter;

    public List<CityDTO> getAll(){
        return cityConverter.listToDTO(cityRepository.findAll());
    }

    public CityDTO saveCity(CityDTO cityDTO) throws Exception{
        return cityConverter.toDto(cityRepository.save(cityConverter.toEntity(cityDTO)));
    }

    @Transactional
    public void deleteCity(int postalcode) throws Exception{
        if (cityRepository.findByPostalCode(postalcode).isEmpty()) {
            throw new Exception("City doesn't exist");
        }
        cityRepository.deleteByPostalCode(postalcode);
    }

    public CityDTO updateCity(@Valid CityDTO cityDTO) throws Exception{
        int postalCode = cityDTO.postalCode();
        Optional<City> foundCity = cityRepository.findByPostalCode(postalCode);
        if (foundCity.isEmpty()) {
            throw new Exception("City doesn't exist");
        }

        City updatedCity = foundCity.get();
        updatedCity.setPopulation(cityDTO.population());
        return cityConverter.toDto(cityRepository.save(updatedCity));
    }
}
