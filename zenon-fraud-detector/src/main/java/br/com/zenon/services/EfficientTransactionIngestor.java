package br.com.zenon.services;

import br.com.zenon.model.Customer;
import br.com.zenon.model.Transaction;
import br.com.zenon.model.TypeTransactionEnum;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EfficientTransactionIngestor {
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

    private static final int LIMIT_TRANSACTIONS_LINES = 10000;
    private static final int LINE_BATCH_SIZE = 5000;

    public void readAsBatch(String filePath, Consumer<List<Transaction>> batchConsumer) {
        Path path = Path.of(filePath);
        try (ExecutorService executorService = Executors.newFixedThreadPool(10);
             Stream<String> lines = Files.lines(path).skip(1)) {

            Iterator<String> iterator = lines.iterator();

            List<String> lineBatch = new ArrayList<>();
            while (iterator.hasNext()) {
                String line = iterator.next();
                lineBatch.add(line);

                if (lineBatch.size() >= LINE_BATCH_SIZE) {
                    IO.println("Processing batch " + lineBatch.size() + " transactions.");
                    final List<String> concurrentLineBatch = List.copyOf(lineBatch);
                    executorService.submit(() -> executeBatch(concurrentLineBatch, batchConsumer));
                    lineBatch.clear();
                }
            }

            if (!lineBatch.isEmpty()) {
                IO.println("Processing final batch " + lineBatch.size() + " transactions.");
                final List<String> concurrentLineBatch = List.copyOf(lineBatch);
                executorService.submit(() -> executeBatch(concurrentLineBatch, batchConsumer));
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void executeBatch(List<String> lineBatch, Consumer<List<Transaction>> batchConsumer) {
        IO.println("Thread: " + Thread.currentThread().getName());
        List<Transaction> transactions = lineBatch.stream()
                .map(this::createTransation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        batchConsumer.accept(transactions);
    }

    public void readAsStream(String filePath, Consumer<Transaction> consumer) {
        Path path = Path.of(filePath);
        try (Stream<String> lines = Files.lines(path)) {
            lines
                    .skip(1L)
                    .limit(LIMIT_TRANSACTIONS_LINES)
                    .map(this::createTransation)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(consumer);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
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
        ValidatorFields.validateIsNumber("step", transactionColumns[STEP_COLUMN]);
        ValidatorFields.validadeRequiredField("type", transactionColumns[TYPE_COLUMN]);
        ValidatorFields.validadeRequiredField("amount", transactionColumns[AMOUNT_COLUMN]);
        ValidatorFields.validateIsNumber("amount", transactionColumns[AMOUNT_COLUMN]);
        ValidatorFields.validadeRequiredField("oldBalanceOrig", transactionColumns[OLD_BALANCE_ORIG_COLUMN]);
        ValidatorFields.validateIsNumber("oldBalanceOrig", transactionColumns[OLD_BALANCE_ORIG_COLUMN]);
        ValidatorFields.validadeRequiredField("newBalanceOrig", transactionColumns[NEW_BALANCE_ORIG_COLUMN]);
        ValidatorFields.validateIsNumber("newBalanceOrig", transactionColumns[NEW_BALANCE_ORIG_COLUMN]);
        ValidatorFields.validadeRequiredField("oldBalanceDest", transactionColumns[OLD_BALANCE_DEST_COLUMN]);
        ValidatorFields.validateIsNumber("oldBalanceDest", transactionColumns[NEW_BALANCE_DEST_COLUMN]);
        ValidatorFields.validadeRequiredField("newBalanceDest", transactionColumns[OLD_BALANCE_DEST_COLUMN]);
        ValidatorFields.validateIsNumber("newBalanceDest", transactionColumns[NEW_BALANCE_DEST_COLUMN]);
    }

}
