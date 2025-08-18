package fon.bg.ac.rs.istanisic.dto;

import java.time.LocalDate;

public record PersonDTO (
        Long id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        Long uniqueIdentificationNumber,
        String cityBirthName,
        String cityResidenceName
){

}
