package br.com.zenon.model;

import br.com.zenon.services.ValidatorFields;

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
