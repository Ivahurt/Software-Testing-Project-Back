package fon.bg.ac.rs.istanisic.converter;

import fon.bg.ac.rs.istanisic.dto.CityDTO;
import fon.bg.ac.rs.istanisic.model.City;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CityConverterTest {
    private final CityConverter cityConverter = new CityConverter();

    @Test
    void testToEntityNotNull() {
        CityDTO dto = new CityDTO(1L, 1111, "Beograd", 70000);

        City entity = cityConverter.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Beograd");
        assertThat(entity.getPostalCode()).isEqualTo(1111);
        assertThat(entity.getPopulation()).isEqualTo(70000);
    }

    @Test
    public void testToDtoNotNull() {
        City city = City.builder()
                .id(2L)
                .postalCode(111111)
                .name("Novi Sad")
                .population(80000)
                .build();

        CityDTO dto = cityConverter.toDto(city);
        assertNotNull(dto);
        assertEquals(city.getId(), dto.id());
        assertEquals(city.getName(), dto.name());
        assertEquals(city.getPostalCode(), dto.postalCode());
        assertEquals(city.getPopulation(), dto.population());
    }

    @Test
    public void testToEntityNull() {
        assertNull(cityConverter.toEntity(null));
    }

    @Test
    public void testToDtoNull() {
        assertNull(cityConverter.toDto(null));
    }


}
