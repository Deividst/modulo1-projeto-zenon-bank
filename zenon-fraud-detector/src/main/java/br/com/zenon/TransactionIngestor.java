package br.com.zenon;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {


    public static List<Transaction> readTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
            String line;
            while ((line = reader.readLine()) != null && transactions.size() <= 1000) {

                if (!line.startsWith("step")) {
                    String[] transactionColumns = line.split(",");

                    transactions.add(new Transaction(
                            Integer.parseInt(transactionColumns[0]),
                            TypeTransactionEnum.valueOf(transactionColumns[1]),
                            new BigDecimal(transactionColumns[2]),
                            new Customer(transactionColumns[3], new BigDecimal(transactionColumns[4]), new BigDecimal(transactionColumns[5])),
                            new Customer(transactionColumns[6], new BigDecimal(transactionColumns[7]), new BigDecimal(transactionColumns[8])),
                            transactionColumns[9].equals("1"), transactionColumns[10].equals("1")
                    ));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return transactions;
    }

}
