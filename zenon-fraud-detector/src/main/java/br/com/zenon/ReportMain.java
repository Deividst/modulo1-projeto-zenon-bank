package br.com.zenon;

public class ReportMain {

    static void main() {
        long tempoInicio = System.nanoTime();
        IO.println("inicio: " + tempoInicio);

        Statistics statistics = new TransactionReport().generateTransactionReport("data/PS_20174392719_1491204439457_log.csv");
        IO.println("Total number of lines: " + statistics.totalTransactions());
        IO.println("Total fraud: " + statistics.totalFrauds());
        IO.println("Total value transacted: " + statistics.totalAmount());

        long tempoFim = System.nanoTime();
        IO.println("fim: " + tempoFim);
        IO.println("Duração em M/S: " + (tempoFim- tempoInicio) / 1_000_000.0);

        IO.println("-----------------------------------------------------------");

        long tempoInicioOldSchool = System.nanoTime();
        IO.println("inicio: " + tempoInicioOldSchool);

        Statistics statisticsOldSchool = new TransactionReport().generateTransactionReportOldSchool("data/PS_20174392719_1491204439457_log.csv");
        IO.println("Total number of lines: " + statisticsOldSchool.totalTransactions());
        IO.println("Total fraud: " + statisticsOldSchool.totalFrauds());
        IO.println("Total value transacted: " + statisticsOldSchool.totalAmount());

        long tempoFimOldSchool = System.nanoTime();
        IO.println("fim: " + tempoFim);
        IO.println("Duração em M/S: " + (tempoFimOldSchool- tempoInicioOldSchool) / 1_000_000.0);


    }
}
