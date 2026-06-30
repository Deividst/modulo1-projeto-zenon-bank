package br.com.zenon;

import br.com.zenon.repository.TransactionRepository;
import br.com.zenon.repository.TransactionSqlRepository;

import java.util.List;

public class DbMain {

    static void main() {

        ConnectionFactory.getConnection();
        IO.println("Connection successfully established.");

        TransactionRepository transactionRepository = new TransactionSqlRepository();
        transactionRepository.findByCustomerOriginName("C100000").ifPresent(System.out::println);

        TransactionIngestor transactionIngestor = new TransactionIngestor();

        long tempoInicio = System.nanoTime();
        IO.println("inicio: " + tempoInicio);

        List<Transaction> transactions = transactionIngestor.readTransactionsStreams("data/PS_20174392719_1491204439457_log.csv");
        transactions.forEach(transactionRepository::save);

        long tempoFim = System.nanoTime();
        IO.println("fim: " + tempoFim);
        IO.println("Duração em M/S: " + (tempoFim- tempoInicio) / 1_000_000.0);
        //Duração em M/S: 637538.2119 para 10k registros
        IO.println("-----------------------------------------------------------");

    }

}
