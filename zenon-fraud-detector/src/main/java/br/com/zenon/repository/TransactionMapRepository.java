package br.com.zenon.repository;

import br.com.zenon.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transactions;

    public TransactionMapRepository(List<Transaction> transactions) {
        this.transactions = transactions.stream().collect(Collectors.toMap(
                transaction -> transaction.origin().name(),
                Function.identity()
        ));
    }

    @Override
    public Optional<Transaction> findByCustomerOriginName(String customerOriginName) {
        return Optional.ofNullable(this.transactions.get(customerOriginName));
    }

    @Override
    public void save(Transaction transaction) {
        this.transactions.putIfAbsent(transaction.origin().name(), transaction);
    }

}
