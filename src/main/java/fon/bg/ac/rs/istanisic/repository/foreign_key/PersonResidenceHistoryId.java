package fon.bg.ac.rs.istanisic.repository.foreign_key;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResidenceHistoryId implements Serializable {
    private Long personId;
    private Long cityId;
    private LocalDate residenceStart;

}
