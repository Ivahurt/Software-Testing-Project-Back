package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.converter.PaymentConverter;
import fon.bg.ac.rs.istanisic.dto.PaymentDTO;
import fon.bg.ac.rs.istanisic.model.Payment;
import fon.bg.ac.rs.istanisic.model.Person;
import fon.bg.ac.rs.istanisic.model.PaymentReason;
import fon.bg.ac.rs.istanisic.repository.PaymentRepository;
import fon.bg.ac.rs.istanisic.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PaymentConverter paymentConverter;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    private PaymentService paymentService;

    private Person person;
    private PaymentDTO paymentDTO;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository, paymentConverter, personRepository);

        ReflectionTestUtils.setField(paymentService, "entityManager", entityManager);

        person = Person.builder()
                .id(1L)
                .firstName("Iva")
                .lastName("Istanisic")
                .uniqueIdentificationNumber(1234567890123L)
                .build();

        paymentDTO = new PaymentDTO(1234567890123L, 1000L, PaymentReason.Plata, LocalDate.now());

        payment = Payment.builder()
                .amount(1000L)
                .reason(PaymentReason.Plata)
                .paymentDate(LocalDate.now())
                .person(person)
                .build();
    }

    @Test
    @DisplayName("Treba da baci izuzetak kada osoba nije pronadjena")
    void savePersonPaymentPersonNotFound() {
        when(personRepository.findByUniqueIdentificationNumber(paymentDTO.uniqueIdentificationNumber()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.savePersonPayment(paymentDTO))
                .isInstanceOf(Exception.class)
                .hasMessage("Osoba ne postoji");

        verify(paymentConverter, never()).toEntity(any());
        verify(entityManager, never()).createNativeQuery(anyString());
    }

    @Test
    @DisplayName("Treba uspešno da sačuva plaćanje kada osoba postoji")
    void savePersonPaymentSuccess() throws Exception {
        when(personRepository.findByUniqueIdentificationNumber(paymentDTO.uniqueIdentificationNumber()))
                .thenReturn(Optional.of(person));
        when(paymentConverter.toEntity(paymentDTO)).thenReturn(payment);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        paymentService.savePersonPayment(paymentDTO);

        verify(personRepository, times(1)).findByUniqueIdentificationNumber(paymentDTO.uniqueIdentificationNumber());
        verify(paymentConverter, times(1)).toEntity(paymentDTO);
        verify(entityManager, times(1)).createNativeQuery(
                "INSERT INTO payment (person_id, payment_id, amount, reason, payment_date) " +
                        "VALUES (:personId, nextval('payment_payment_id_seq'), :amount, :reason, :paymentDate)"
        );
        verify(query, times(1)).setParameter("personId", person.getId());
        verify(query, times(1)).setParameter("amount", payment.getAmount());
        verify(query, times(1)).setParameter("reason", payment.getReason().name());
        verify(query, times(1)).setParameter("paymentDate", payment.getPaymentDate());
        verify(query, times(1)).executeUpdate();

        assertThat(payment.getPerson()).isEqualTo(person);
    }

    @Test
    @DisplayName("Treba da baci izuzetak kada nema plaćanja")
    void getPersonPaymentsNoPayments() {
        Long uniqueId = person.getUniqueIdentificationNumber();
        when(paymentRepository.findByPersonUniqueIdentificationNumber(uniqueId))
                .thenReturn(List.of());

        assertThatThrownBy(() -> paymentService.getPersonPayments(uniqueId))
                .isInstanceOf(Exception.class)
                .hasMessage("Ne postoji isplata za osobu sa jmbg-om: " + uniqueId);

        verify(paymentConverter, never()).listToDTO(any());
    }

    @Test
    @DisplayName("Treba da vrati plaćanja kada osoba ima plaćanja")
    void getPersonPaymentsSuccess() throws Exception {
        Long uniqueId = person.getUniqueIdentificationNumber();
        List<Payment> payments = List.of(payment);
        List<PaymentDTO> expectedPaymentDTOs = List.of(paymentDTO);

        when(paymentRepository.findByPersonUniqueIdentificationNumber(uniqueId))
                .thenReturn(payments);
        when(paymentConverter.listToDTO(payments)).thenReturn(expectedPaymentDTOs);

        List<PaymentDTO> result = paymentService.getPersonPayments(uniqueId);

        assertThat(result).isEqualTo(expectedPaymentDTOs);
        verify(paymentRepository, times(1)).findByPersonUniqueIdentificationNumber(uniqueId);
        verify(paymentConverter, times(1)).listToDTO(payments);
    }

    @Test
    @DisplayName("Treba da postavi osobu na payment entitet")
    void savePersonPaymentShouldSetPersonOnPayment() throws Exception {
        Payment paymentWithoutPerson = Payment.builder()
                .amount(1000L)
                .reason(PaymentReason.Plata)
                .paymentDate(LocalDate.now())
                .build();

        when(personRepository.findByUniqueIdentificationNumber(paymentDTO.uniqueIdentificationNumber()))
                .thenReturn(Optional.of(person));
        when(paymentConverter.toEntity(paymentDTO)).thenReturn(paymentWithoutPerson);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        paymentService.savePersonPayment(paymentDTO);

        assertThat(paymentWithoutPerson.getPerson()).isEqualTo(person);
        verify(paymentConverter, times(1)).toEntity(paymentDTO);
    }

    @Test
    @DisplayName("Treba da rukuje višestrukim plaćanjima ispravno")
    void getPersonPaymentsMultiplePayments() throws Exception {
        Long uniqueId = person.getUniqueIdentificationNumber();

        Payment payment1 = Payment.builder()
                .amount(1000L)
                .reason(PaymentReason.Plata)
                .paymentDate(LocalDate.now())
                .person(person)
                .build();

        Payment payment2 = Payment.builder()
                .amount(2000L)
                .reason(PaymentReason.Plata)
                .paymentDate(LocalDate.now().minusDays(1))
                .person(person)
                .build();

        List<Payment> payments = List.of(payment1, payment2);

        PaymentDTO paymentDTO1 = new PaymentDTO(uniqueId, 1000L, PaymentReason.Plata, LocalDate.now());
        PaymentDTO paymentDTO2 = new PaymentDTO(uniqueId, 2000L, PaymentReason.Plata, LocalDate.now().minusDays(1));
        List<PaymentDTO> expectedPaymentDTOs = List.of(paymentDTO1, paymentDTO2);

        when(paymentRepository.findByPersonUniqueIdentificationNumber(uniqueId))
                .thenReturn(payments);
        when(paymentConverter.listToDTO(payments)).thenReturn(expectedPaymentDTOs);

        List<PaymentDTO> result = paymentService.getPersonPayments(uniqueId);

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expectedPaymentDTOs);
        verify(paymentRepository, times(1)).findByPersonUniqueIdentificationNumber(uniqueId);
        verify(paymentConverter, times(1)).listToDTO(payments);
    }
}