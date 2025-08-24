package fon.bg.ac.rs.istanisic.dto;

import java.time.LocalDate;

public record PersonResidenceHistoryDTO (
        String firstName,
        String lastName,
        String cityResidenceName,
        LocalDate residenceStart,
        LocalDate residenceEnd
){

}
