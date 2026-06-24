package br.com.zenon;

import java.math.BigDecimal;

public record Customer(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {

    public Customer {
        ValidatorFields.validadeRequiredField("name", name);
        ValidatorFields.validadeNumberPositive("oldBalance", oldBalance);
        ValidatorFields.validadeNumberPositive("newBalance", newBalance);
    }
}
