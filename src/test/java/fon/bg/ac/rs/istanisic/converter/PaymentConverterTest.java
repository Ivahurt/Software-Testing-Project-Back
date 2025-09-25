package fon.bg.ac.rs.istanisic.converter;

import fon.bg.ac.rs.istanisic.dto.PaymentDTO;
import fon.bg.ac.rs.istanisic.model.Payment;
import fon.bg.ac.rs.istanisic.model.PaymentReason;
import fon.bg.ac.rs.istanisic.model.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentConverterTest {

    private final PaymentConverter converter = new PaymentConverter();

    @Test
    void testToEntityNotNull() {
        PaymentDTO dto = new PaymentDTO(
                1234567890123L,
                5000L,
                PaymentReason.Plata,
                LocalDate.of(2023, 5, 20)
        );

        Payment entity = converter.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getPerson()).isNotNull();
        assertThat(entity.getPerson().getUniqueIdentificationNumber()).isEqualTo(1234567890123L);
        assertThat(entity.getAmount()).isEqualTo(5000L);
        assertThat(entity.getReason()).isEqualTo(PaymentReason.Plata);
        assertThat(entity.getPaymentDate()).isEqualTo(LocalDate.of(2023, 5, 20));
    }

    @Test
    void testToEntityNull() {
        assertNull(converter.toEntity(null));
    }

    @Test
    void testToDtoNotNull() {
        Person person = Person.builder()
                .uniqueIdentificationNumber(1234567890123L)
                .build();

        Payment payment = Payment.builder()
                .person(person)
                .amount(7000L)
                .reason(PaymentReason.Praksa)
                .paymentDate(LocalDate.of(2024, 1, 15))
                .build();

        PaymentDTO dto = converter.toDto(payment);

        assertThat(dto).isNotNull();
        assertThat(dto.uniqueIdentificationNumber()).isEqualTo(1234567890123L);
        assertThat(dto.amount()).isEqualTo(7000L);
        assertThat(dto.reason()).isEqualTo(PaymentReason.Praksa);
        assertThat(dto.paymentDate()).isEqualTo(LocalDate.of(2024, 1, 15));
    }

    @Test
    void testToDtoNull() {
        assertNull(converter.toDto(null));
    }
}