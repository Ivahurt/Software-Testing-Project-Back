package fon.bg.ac.rs.istanisic.dto;

import fon.bg.ac.rs.istanisic.model.PaymentReason;

import java.time.LocalDate;

public record PaymentDTO (
        Long uniqueIdentificationNumber,
        Long amount,
        PaymentReason reason,
        LocalDate paymentDate
) {
}