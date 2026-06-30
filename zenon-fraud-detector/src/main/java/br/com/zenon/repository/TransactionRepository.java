package br.com.zenon.repository;

import br.com.zenon.Transaction;

import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> findByCustomerOriginName(String customerOriginName);

    void save(Transaction transaction);

}
