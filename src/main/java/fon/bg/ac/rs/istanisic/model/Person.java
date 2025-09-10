package fon.bg.ac.rs.istanisic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 30, min = 2, message = "Ime osobe mora imati između 2 i 30 karaktera.")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(max = 30, min = 2, message = "Prezime osobe mora imati između 2 i 30 karaktera.")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "age_in_months")
    private Integer ageInMonths;

    @NotNull
    @Column(name = "unique_identification_number")
    private Long uniqueIdentificationNumber;

    @ManyToOne
    @JoinColumn(name = "city_birth_id")
    private City cityOfBirth;

    @ManyToOne
    @JoinColumn(name = "city_residence_id")
    private City cityOfResidence;

}

