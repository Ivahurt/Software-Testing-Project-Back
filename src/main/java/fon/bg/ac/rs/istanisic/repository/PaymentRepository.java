package fon.bg.ac.rs.istanisic.repository;

import fon.bg.ac.rs.istanisic.model.Payment;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.repository.foreign_key.PaymentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, PaymentId> {
    List<Payment> findByPersonUniqueIdentificationNumber(Long uniqueId);

}
