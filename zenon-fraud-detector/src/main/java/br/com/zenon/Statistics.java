package br.com.zenon;

import java.math.BigDecimal;

public record Statistics(long totalTransactions, long totalFrauds, BigDecimal totalAmount) {

    final static Statistics ZERO = new Statistics(0, 0, BigDecimal.ZERO);

    Statistics addReportTransaction(ReportTransactionDto reportTransaction) {
        return new Statistics(
                totalTransactions + 1,
                totalFrauds() + (reportTransaction.isFraud() ? 1 : 0),
                totalAmount().add(reportTransaction.amount())
        );
    }

    Statistics add(Statistics statistics) {
        return new Statistics(
                totalTransactions + statistics.totalTransactions,
                totalFrauds + statistics.totalFrauds,
                totalAmount().add(statistics.totalAmount())
        );
    }

}
