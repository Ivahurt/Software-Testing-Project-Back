package fon.bg.ac.rs.istanisic.model;

import fon.bg.ac.rs.istanisic.repository.foreign_key.PaymentId;
import jakarta.persistence.*;
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
@Table(name = "payment")
public class Payment {
    @EmbeddedId
    public PaymentId id;

    @ManyToOne
    @MapsId("personId")
    @JoinColumn(name = "person_id", nullable = false)
    public Person person;

    @NotNull(message = "Iznos mora biti unet")
    @Min(value = 1, message = "Iznos mora biti veći od nule")
    @Column(nullable = false)
    public Long amount;

    @NotNull(message = "Razlog isplate mora biti unet")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public PaymentReason reason;

    @NotNull(message = "Datum isplate mora biti unet")
    @Column(name = "payment_date", nullable = false)
    public LocalDate paymentDate;
}
