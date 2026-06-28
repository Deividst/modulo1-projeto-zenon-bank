package br.com.zenon;

import java.math.BigDecimal;

public record ReportTransactionDto(
        BigDecimal amount,
        boolean isFraud
) {

    public ReportTransactionDto {
        ValidatorFields.validadeNumberPositive("amount", amount);
    }
}
