package fon.bg.ac.rs.istanisic.converter;

import fon.bg.ac.rs.istanisic.dto.CityDTO;
import fon.bg.ac.rs.istanisic.model.City;
import org.springframework.stereotype.Component;

@Component
public class CityConverter implements DTOEntityConverter<CityDTO, City> {

    @Override
    public City toEntity(CityDTO cityDTO) {
        return cityDTO == null ? null :
                City.builder()
                        .id(cityDTO.id())
                        .postalCode(cityDTO.postalCode())
                        .name(cityDTO.name())
                        .population(cityDTO.population()).build();
    }

    @Override
    public CityDTO toDto(City city) {
        return city == null ? null : new CityDTO(
                city.getId(),
                city.getPostalCode(),
                city.getName(),
                city.getPopulation()
        );
    }
}
