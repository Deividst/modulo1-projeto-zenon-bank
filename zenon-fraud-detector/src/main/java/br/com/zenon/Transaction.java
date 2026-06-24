package br.com.zenon;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(
        int step,
        TypeTransactionEnum type,
        BigDecimal amount,
        Customer origin,
        Customer recipient,
        boolean isFraud,
        boolean isFlaggedFraud
) {

    public Transaction {
        ValidatorFields.validadeNumberPositive("step", step);
        ValidatorFields.validadeNumberPositive("amount", amount);
        Objects.requireNonNull(origin, "origin should not be null");
        Objects.requireNonNull(recipient, "recipient should not be null");
    }
}
