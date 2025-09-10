package fon.bg.ac.rs.istanisic.converter;

import fon.bg.ac.rs.istanisic.dto.PaymentDTO;
import fon.bg.ac.rs.istanisic.model.Payment;
import fon.bg.ac.rs.istanisic.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter implements DTOEntityConverter<PaymentDTO, Payment>{
    @Override
    public Payment toEntity(PaymentDTO paymentDTO) {
        return paymentDTO == null ? null :
                Payment.builder()
                        .person(Person.builder()
                                .uniqueIdentificationNumber(paymentDTO.uniqueIdentificationNumber())
                                .build())
                        .amount(paymentDTO.amount())
                        .reason(paymentDTO.reason())
                        .paymentDate(paymentDTO.paymentDate())
                        .build();
    }

    @Override
    public PaymentDTO toDto(Payment payment) {
        return payment == null ? null :
                new PaymentDTO(
                        payment.getPerson().getUniqueIdentificationNumber(),
                        payment.getAmount(),
                        payment.getReason(),
                        payment.getPaymentDate()
                );
    }
}
