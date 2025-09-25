package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.converter.CityConverter;
import fon.bg.ac.rs.istanisic.dto.CityDTO;
import fon.bg.ac.rs.istanisic.dto.CityUpdateDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.repository.CityRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityConverter cityConverter;

    @InjectMocks
    private CityService cityService;

    @Test
    @DisplayName("Get all cities empty")
    void getAllCitiesEmpty() {
        when(cityRepository.findAll()).thenReturn(List.of());
        when(cityConverter.listToDTO(List.of())).thenReturn(List.of());

        var result = cityService.getAll();

        assertThat(result).isEmpty();
        verify(cityRepository, times(1)).findAll();
        verify(cityConverter, times(1)).listToDTO(List.of());
    }

    @Test
    @DisplayName("Save city successfully")
    void saveCitySuccess() throws Exception {
        CityDTO dto = new CityDTO(1L, 11000, "Beograd", 1500000);
        City entity = new City(1L, 1000, "Beograd", 1500000);

        when(cityConverter.toEntity(dto)).thenReturn(entity);
        when(cityRepository.save(entity)).thenReturn(entity);
        when(cityConverter.toDto(entity)).thenReturn(dto);

        var result = cityService.saveCity(dto);

        assertThat(result).isEqualTo(dto);
        verify(cityRepository, times(1)).save(entity);
    }

    @Test
    @DisplayName("Delete non existing city throws exception")
    void deleteNonExistingCity() {
        when(cityRepository.findByPostalCode(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.deleteCity(99999))
                .isInstanceOf(Exception.class)
                .hasMessage("Mesto ne postoji");

        verify(cityRepository, never()).deleteByPostalCode(99999);
    }

    @Test
    @DisplayName("Delete existing city success")
    void deleteExistingCity() throws Exception {
        int postalCode = 11000;
        City city = new City(1L, postalCode, "Beograd", 1500000);

        when(cityRepository.findByPostalCode(postalCode)).thenReturn(Optional.of(city));

        cityService.deleteCity(postalCode);

        verify(cityRepository, times(1)).deleteByPostalCode(postalCode);
    }

    @Test
    @DisplayName("Update non existing city throws exception")
    void updateNonExistingCity() {
        CityUpdateDTO updateDTO = new CityUpdateDTO(99999, 1000);

        when(cityRepository.findByPostalCode(updateDTO.postalCode())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.updateCity(updateDTO))
                .isInstanceOf(Exception.class)
                .hasMessage("Mesto ne postoji");

        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    @DisplayName("Update city successfully")
    void updateCitySuccess() throws Exception {
        CityUpdateDTO updateDTO = new CityUpdateDTO(11000, 2000000);
        City entity = new City(1L, 11000, "Beograd", 1500000);
        CityDTO returnedDTO = new CityDTO(1L, 11000, "Beograd", 2000000);

        when(cityRepository.findByPostalCode(updateDTO.postalCode())).thenReturn(Optional.of(entity));
        when(cityRepository.save(entity)).thenReturn(entity);
        when(cityConverter.toDto(entity)).thenReturn(returnedDTO);

        var result = cityService.updateCity(updateDTO);

        assertThat(result).isEqualTo(returnedDTO);
        assertThat(entity.getPopulation()).isEqualTo(2000000);

        verify(cityRepository, times(1)).save(entity);
        verify(cityConverter, times(1)).toDto(entity);
    }

}