package fon.bg.ac.rs.istanisic.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PersonUpdateDTO (
        @NotNull  Long uniqueIdentificationNumber,
        @NotNull  String cityResidenceName
){

}
