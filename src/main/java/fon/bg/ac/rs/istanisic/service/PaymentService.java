package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.converter.PaymentConverter;
import fon.bg.ac.rs.istanisic.dto.PaymentDTO;
import fon.bg.ac.rs.istanisic.model.Payment;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.repository.PaymentRepository;
import fon.bg.ac.rs.istanisic.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentConverter paymentConverter;
    private final PersonRepository personRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void savePersonPayment(PaymentDTO paymentDTO) throws Exception{
        Long uniqueIdentificationNumber = paymentDTO.uniqueIdentificationNumber();
        Optional<Person> foundPerson = personRepository.findByUniqueIdentificationNumber(uniqueIdentificationNumber);
        if (foundPerson.isEmpty()) {
            throw new Exception("Osoba ne postoji");
        }

        Payment payment = paymentConverter.toEntity(paymentDTO);
        payment.setPerson(foundPerson.get());

        entityManager.createNativeQuery(
                        "INSERT INTO payment (person_id, payment_id, amount, reason, payment_date) " +
                                "VALUES (:personId, nextval('payment_payment_id_seq'), :amount, :reason, :paymentDate)"
                )
                .setParameter("personId", foundPerson.get().getId())
                .setParameter("amount", payment.getAmount())
                .setParameter("reason", payment.getReason().name())
                .setParameter("paymentDate", payment.getPaymentDate())
                .executeUpdate();
    }

    public List<PaymentDTO> getPersonPayments(Long uniqueId) throws Exception{
        List<Payment> payments = paymentRepository.findByPersonUniqueIdentificationNumber(uniqueId);
        if (payments.isEmpty()) {
            throw new Exception("Ne postoji osoba sa jmbg-om: "+ uniqueId);
        }

        return paymentConverter.listToDTO(payments);
    }
}
