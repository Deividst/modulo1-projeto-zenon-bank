package br.com.zenon.dtos;

import br.com.zenon.services.ValidatorFields;

import java.math.BigDecimal;

public record ReportTransactionDto(
        BigDecimal amount,
        boolean isFraud
) {

    public ReportTransactionDto {
        ValidatorFields.validadeNumberPositive("amount", amount);
    }
}
