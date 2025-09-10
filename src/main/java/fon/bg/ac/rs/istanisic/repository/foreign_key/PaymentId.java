package fon.bg.ac.rs.istanisic.repository.foreign_key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentId {
    @Column(name = "person_id")
    public Long personId;

    @Column(name = "payment_id")
    public Long paymentId;
}
