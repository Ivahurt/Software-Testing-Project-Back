package fon.bg.ac.rs.istanisic.repository;

import fon.bg.ac.rs.istanisic.model.Payment;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.PaymentReason;
import fon.bg.ac.rs.istanisic.repository.foreign_key.PaymentId;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PersonRepository personRepository;

    private Person examplePerson;
    private Payment examplePayment;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        personRepository.deleteAll();

        examplePerson = Person.builder()
                .uniqueIdentificationNumber(123456789L)
                .firstName("Nikola")
                .lastName("Istanisic")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
        personRepository.save(examplePerson);

        examplePayment = Payment.builder()
                .id(new PaymentId(examplePerson.getUniqueIdentificationNumber(), 1L))
                .person(examplePerson)
                .amount(1000L)
                .reason(PaymentReason.Plata)
                .paymentDate(LocalDate.of(2025, 9, 25))
                .build();

        paymentRepository.save(examplePayment);
    }

    @Test
    void findByPersonUniqueIdentificationNumberFound() {
        List<Payment> payments = paymentRepository.findByPersonUniqueIdentificationNumber(123456789L);
        assertThat(payments).isNotEmpty();
        assertThat(payments).contains(examplePayment);
    }

    @Test
    void findByPersonUniqueIdentificationNumberNotFound() {
        List<Payment> payments = paymentRepository.findByPersonUniqueIdentificationNumber(987654321L);
        assertThat(payments).isEmpty();
    }

    @Test
    void deletePayment() {
        PaymentId paymentId = examplePayment.getId();
        paymentRepository.deleteById(paymentId);

        Optional<Payment> deleted = paymentRepository.findById(paymentId);
        assertThat(deleted).isEmpty();
    }
}