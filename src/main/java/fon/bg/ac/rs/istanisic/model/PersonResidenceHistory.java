package fon.bg.ac.rs.istanisic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PersonResidenceHistory {
    @NotNull
    @Column(name = "person_id")
    private  Person person;

    @NotNull
    @Column(name = "city_id")
    private City city;

    @NotNull
    @Column(name = "residence_start")
    private LocalDate residenceStart;

    @Column(name = "residence_end")
    private LocalDate residenceEnd;
}
