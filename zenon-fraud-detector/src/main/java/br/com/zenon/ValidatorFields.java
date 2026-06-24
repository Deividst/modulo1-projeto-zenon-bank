package br.com.zenon;

import java.math.BigDecimal;
import java.util.Objects;

public class ValidatorFields {

    public static void validadeRequiredField(String fieldName, String fieldValue) {
        if (Objects.isNull(fieldValue) || fieldValue.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " should not be null or empty: " + fieldValue);
        }
    }

    public static void validadeIsNumber(String fieldName, String fieldValue) {
        if (!fieldValue.matches("^-?\\d+(\\.\\d+)?$")) {
            throw new IllegalArgumentException(fieldName + " should be a number: " + fieldValue);
        }
    }

    public static void validadeNumberPositive(String fieldName, int fieldValue) {
        if (fieldValue < 1) {
            throw new IllegalArgumentException(fieldName + " should be positive:: " + fieldValue);
        }
    }

    public static void validadeNumberPositive(String fieldName, BigDecimal fieldValue) {
        if (fieldValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + " should be positive:: " + fieldValue);
        }
    }

}
