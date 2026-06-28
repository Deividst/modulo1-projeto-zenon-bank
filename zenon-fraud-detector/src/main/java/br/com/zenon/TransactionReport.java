package br.com.zenon;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {

    private static final int AMOUNT_COLUMN = 2;
    private static final int IS_FRAUD_COLUMN = 9;

    public Statistics generateTransactionReport(String filePath) {
        Path path = Path.of(filePath);
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1L)
                    .map(this::mapToReportTransactionDto)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(Statistics.ZERO, Statistics::addReportTransaction,  Statistics::add);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Statistics generateTransactionReportOldSchool(String filePath) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {

            Statistics statistics = new Statistics(0, 0, BigDecimal.ZERO);
            String line;
            reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {

                Optional<ReportTransactionDto> dto =
                        mapToReportTransactionDto(line);

                if (dto.isPresent()) {

                    ReportTransactionDto rt = dto.get();

                    statistics = new Statistics(
                            statistics.totalTransactions() + 1,
                            statistics.totalFrauds() + (rt.isFraud() ? 1 : 0),
                            statistics.totalAmount().add(rt.amount())
                    );
                }
            }

            return statistics;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Optional<ReportTransactionDto> mapToReportTransactionDto(String line) {
        try {
            String[] transactionColumns = line.split(",");

            return Optional.of(new ReportTransactionDto(
                    new BigDecimal(transactionColumns[AMOUNT_COLUMN]),
                    transactionColumns[IS_FRAUD_COLUMN].equals("1")
            ));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + line + " | " + e.getMessage());
        }
        return Optional.empty();
    }

}
