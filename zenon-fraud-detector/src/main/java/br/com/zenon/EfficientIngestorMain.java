package br.com.zenon;

import br.com.zenon.repository.TransactionSqlRepository;
import br.com.zenon.services.EfficientTransactionIngestor;

public class EfficientIngestorMain {

    static void main() {

        TransactionSqlRepository transactionRepository = new TransactionSqlRepository();
        EfficientTransactionIngestor transactionIngestor = new EfficientTransactionIngestor();

        long tempoInicio = System.nanoTime();
        IO.println("inicio: " + tempoInicio);

        transactionIngestor.readAsBatch("data/PS_20174392719_1491204439457_log.csv", transactionRepository::saveAll);

        long tempoFim = System.nanoTime();
        IO.println("fim: " + tempoFim);
        IO.println("Duração em M/S: " + (tempoFim- tempoInicio) / 1_000_000.0);
        //Duração em M/S: 637538.2119 para 10k registros
        //Duração em M/S com uma conexão: 31707.2904 para 10k registros
        //Duração em M/S com uma conexão e salvando em lotes de 1k: 3434.8946 para 10k registros
        //Duração em M/S com uma conexão e salvando em lotes e utilizando rewriteBatchedStatements de 1k: 1574.6066 para 10k registros
        //Duração em M/S com efficientIngestor: 964.6774 para 10k registros
        IO.println("-----------------------------------------------------------");

    }

}
