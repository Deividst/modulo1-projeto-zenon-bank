package br.com.zenon;

import java.math.BigDecimal;

public record Transaction(
        int step,
        TypeTransactionEnum type,
        BigDecimal amount,
        Customer origin,
        Customer recipient,
        boolean isFraud,
        boolean isFlaggedFraud
) {
}
