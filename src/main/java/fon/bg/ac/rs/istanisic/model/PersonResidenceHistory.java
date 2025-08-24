package fon.bg.ac.rs.istanisic.model;

import fon.bg.ac.rs.istanisic.repository.foreign_key.PersonResidenceHistoryId;
import jakarta.persistence.*;
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

    @EmbeddedId
    private PersonResidenceHistoryId personResidenceHistoryId;

    @MapsId("personId")
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @MapsId("cityId")
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "residence_end")
    private LocalDate residenceEnd;

}
