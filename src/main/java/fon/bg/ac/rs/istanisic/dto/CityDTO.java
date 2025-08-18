package fon.bg.ac.rs.istanisic.dto;

public record CityDTO(
        Long id,
        int postalCode,
        String name,
        Integer population
) {
}
