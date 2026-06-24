package br.com.zenon;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    private static final int STEP_COLUMN = 0;
    private static final int TYPE_COLUMN = 1;
    private static final int AMOUNT_COLUMN = 2;
    private static final int NAME_ORIG_COLUMN = 3;
    private static final int OLD_BALANCE_ORIG_COLUMN = 4;
    private static final int NEW_BALANCE_ORIG_COLUMN = 5;
    private static final int NAME_DEST_COLUMN = 6;
    private static final int OLD_BALANCE_DEST_COLUMN = 7;
    private static final int NEW_BALANCE_DEST_COLUMN = 8;
    private static final int IS_FRAUD_COLUMN = 9;
    private static final int IS_FLAGGED_FRAUD_COLUMN = 10;

    public List<Transaction> readTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
            String line;
            while ((line = reader.readLine()) != null && transactions.size() <= 1000) {

                if (!line.startsWith("step")) {
                    createTransation(line).ifPresent(transactions::add);
                }

            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return transactions;
    }

    private Optional<Transaction> createTransation(String line) {
        try {
            String[] transactionColumns = line.split(",");
            preValidateFields(transactionColumns);
            return Optional.of(new Transaction(
                    Integer.parseInt(transactionColumns[STEP_COLUMN]),
                    TypeTransactionEnum.valueOf(transactionColumns[TYPE_COLUMN]),
                    new BigDecimal(transactionColumns[AMOUNT_COLUMN]),
                    new Customer(transactionColumns[NAME_ORIG_COLUMN], new BigDecimal(transactionColumns[OLD_BALANCE_ORIG_COLUMN]), new BigDecimal(transactionColumns[NEW_BALANCE_ORIG_COLUMN])),
                    new Customer(transactionColumns[NAME_DEST_COLUMN], new BigDecimal(transactionColumns[OLD_BALANCE_DEST_COLUMN]), new BigDecimal(transactionColumns[NEW_BALANCE_DEST_COLUMN])),
                    transactionColumns[IS_FRAUD_COLUMN].equals("1"), transactionColumns[IS_FLAGGED_FRAUD_COLUMN].equals("1")
            ));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + line + " | " + e.getMessage());
        }
        return Optional.empty();
    }

    private void preValidateFields(String[] transactionColumns) {
        ValidatorFields.validadeRequiredField("step", transactionColumns[STEP_COLUMN]);
        ValidatorFields.validadeIsNumber("step", transactionColumns[STEP_COLUMN]);
        ValidatorFields.validadeRequiredField("type", transactionColumns[TYPE_COLUMN]);
        ValidatorFields.validadeRequiredField("amount", transactionColumns[AMOUNT_COLUMN]);
        ValidatorFields.validadeIsNumber("amount", transactionColumns[AMOUNT_COLUMN]);
        ValidatorFields.validadeRequiredField("oldBalanceOrig", transactionColumns[OLD_BALANCE_ORIG_COLUMN]);
        ValidatorFields.validadeIsNumber("oldBalanceOrig", transactionColumns[OLD_BALANCE_ORIG_COLUMN]);
        ValidatorFields.validadeRequiredField("newBalanceOrig", transactionColumns[NEW_BALANCE_ORIG_COLUMN]);
        ValidatorFields.validadeIsNumber("newBalanceOrig", transactionColumns[NEW_BALANCE_ORIG_COLUMN]);
        ValidatorFields.validadeRequiredField("oldBalanceDest", transactionColumns[OLD_BALANCE_DEST_COLUMN]);
        ValidatorFields.validadeIsNumber("oldBalanceDest", transactionColumns[NEW_BALANCE_DEST_COLUMN]);
        ValidatorFields.validadeRequiredField("newBalanceDest", transactionColumns[OLD_BALANCE_DEST_COLUMN]);
        ValidatorFields.validadeIsNumber("newBalanceDest", transactionColumns[NEW_BALANCE_DEST_COLUMN]);
    }
}
