package br.com.zenon;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    void main() {

        Transaction transaction1 = new Transaction(1,
                TypeTransactionEnum.PAYMENT,
                new BigDecimal("9839.64"),
                new Customer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
                new Customer("M1979787155", BigDecimal.ZERO, BigDecimal.ZERO),
                false,
                false);

        Transaction transaction2 = new Transaction(743,
                TypeTransactionEnum.CASH_OUT,
                new BigDecimal("850002.52"),
                new Customer("C1280323807", new BigDecimal("850002.52"), BigDecimal.ZERO),
                new Customer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true,
                false);

        IO.println(transaction1);
        IO.println(transaction2);

        IO.println("----------------------------------------------------------------------------------------------------");

        List<Transaction> transactionsSucess = new TransactionIngestor().readTransactionsOldSchool("data/PS_20174392719_1491204439457_log.csv");
        transactionsSucess.forEach(System.out::println);

        IO.println("----------------------------------------------------------------------------------------------------");

        List<Transaction> transactionsErrors = new TransactionIngestor().readTransactionsOldSchool("data/paysim_with_bad_data.csv");
        transactionsErrors.forEach(System.out::println);

        IO.println("----------------------------------------------------------------------------------------------------");

        List<Transaction> transactions = new TransactionIngestor().readTransactionsOldSchool("data/PS_20174392719_1491204439457_log.csv");
        FraudAnalyzer.numberFraudulentTransactions(transactions);
        FraudAnalyzer.top3MostValuableFrauds(transactions);
        FraudAnalyzer.namesSuspectClients(transactions);
        FraudAnalyzer.calculateTotalLoss(transactions);
        FraudAnalyzer.numberFraudsByType(transactions);

        IO.println("----------------------------------------------------------------------------------------------------");
    }
}