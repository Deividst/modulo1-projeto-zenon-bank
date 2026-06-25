package br.com.zenon;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    public static void numberFraudulentTransactions(List<Transaction> transactions) {
        IO.println("1. Total Frauds: " + transactions.stream().filter(Transaction::isFraud).count());
    }

    public static void top3MostValuableFrauds(List<Transaction> transactions) {
        IO.println("2. Top 3 Most Valuable Frauds:");
        transactions.stream()
                .filter(Transaction::isFraud)
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .map(Transaction::amount)
                .forEach(value -> IO.println(value.toPlainString()));
    }

    public static void namesSuspectClients(List<Transaction> transactions) {
        IO.println("3. Suspicious Customers:");
        transactions.stream()
                .filter(Transaction::isFraud)
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(5)
                .forEach(IO::println);
    }

    public static void calculateTotalLoss(List<Transaction> transactions) {
        IO.print("4. Total Loss: ");
        transactions.stream()
                .filter(Transaction::isFraud)
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(Transaction::amount)
                .reduce(BigDecimal::add)
                .ifPresentOrElse(value -> IO.print(value.toPlainString()), IO::println);
        IO.println();
    }

    public static void numberFraudsByType(List<Transaction> transactions) {
        IO.println("5. Fraud by Type: ");
        transactions.stream()
                .filter(Transaction::isFraud)
                .collect(Collectors.groupingBy(
                        Transaction::type,
                        Collectors.counting()
                ))
                .forEach((key, value) -> IO.println("- " + key + ": " + value));


    }

}
