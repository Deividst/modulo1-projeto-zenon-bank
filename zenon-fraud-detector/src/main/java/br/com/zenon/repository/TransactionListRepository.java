package br.com.zenon.repository;

import br.com.zenon.model.Transaction;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

    private final List<Transaction> transactions;

    public TransactionListRepository(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Optional<Transaction> findByCustomerOriginName(String customerOriginName) {
        return this.transactions.stream()
                .filter(transaction -> transaction.origin().name().equalsIgnoreCase(customerOriginName))
                .findFirst();
    }

    @Override
    public void save(Transaction transaction) {
        this.transactions.add(transaction);
    }

}
