package fon.bg.ac.rs.istanisic.dto;

import jakarta.validation.constraints.NotNull;


public record CityUpdateDTO (
        int postalCode,
        Integer population
){

}