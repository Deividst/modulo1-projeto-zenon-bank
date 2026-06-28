package br.com.zenon;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain {

    static void main() {
        Locale locale = Locale.of("pt", "BR");
        NumberFormat integerFormatter = NumberFormat.getIntegerInstance(locale);
        NumberFormat currencyFormatter = DecimalFormat.getCurrencyInstance(locale);
        currencyFormatter.setCurrency(Currency.getInstance("USD"));

        ResourceBundle resourceBundle = ResourceBundle.getBundle("report", locale);

        long tempoInicio = System.nanoTime();
        IO.println("inicio: " + tempoInicio);

        Statistics statistics = new TransactionReport().generateTransactionReport("data/PS_20174392719_1491204439457_log.csv");
        IO.println("""
                %s: %s
                %s: %s
                %s: %s
                """.formatted(
                resourceBundle.getString("total.transactions"), integerFormatter.format(statistics.totalTransactions()),
                resourceBundle.getString("total.fraud"), integerFormatter.format(statistics.totalFrauds()),
                resourceBundle.getString("total.value.transacted"), currencyFormatter.format(statistics.totalAmount())
        ));

        long tempoFim = System.nanoTime();
        IO.println("fim: " + tempoFim);
        IO.println("Duração em M/S: " + (tempoFim- tempoInicio) / 1_000_000.0);

        IO.println("-----------------------------------------------------------");

        long tempoInicioOldSchool = System.nanoTime();
        IO.println("inicio: " + tempoInicioOldSchool);

        Statistics statisticsOldSchool = new TransactionReport().generateTransactionReportOldSchool("data/PS_20174392719_1491204439457_log.csv");
        IO.println("""
                %s: %s
                %s: %s
                %s: %s
                """.formatted(
                resourceBundle.getString("total.transactions"), integerFormatter.format(statisticsOldSchool.totalTransactions()),
                resourceBundle.getString("total.fraud"), integerFormatter.format(statisticsOldSchool.totalFrauds()),
                resourceBundle.getString("total.value.transacted"), currencyFormatter.format(statisticsOldSchool.totalAmount())
        ));

        long tempoFimOldSchool = System.nanoTime();
        IO.println("fim: " + tempoFim);
        IO.println("Duração em M/S: " + (tempoFimOldSchool- tempoInicioOldSchool) / 1_000_000.0);


        System.out.println("file.encoding = " + System.getProperty("file.encoding"));
        System.out.println("stdout charset = " + System.out.charset());
        System.out.println("default charset = " + java.nio.charset.Charset.defaultCharset());

        System.out.println("ação");
        System.out.println("transações");

    }
}
