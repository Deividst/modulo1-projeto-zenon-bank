package br.com.zenon.repository;

import br.com.zenon.ConnectionFactory;
import br.com.zenon.Customer;
import br.com.zenon.Transaction;
import br.com.zenon.TypeTransactionEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TransactionSqlRepository implements TransactionRepository {


    @Override
    public Optional<Transaction> findByCustomerOriginName(String customerOriginName) {

        String query = """
                SELECT id, step, `type`, amount, name_origin, old_balance_origin, new_balance_origin,
                name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud
                FROM zenon_frauds.TRANSACTIONS
                WHERE name_origin = ?
                ORDER BY step
                LIMIT 1;
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, customerOriginName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToTransaction(resultSet);
                } else {
                    IO.println("No transaction found.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error querying transaction.", e);
        }

        return Optional.empty();
    }

    @Override
    public void save(Transaction transaction) {
        String query = """
                INSERT INTO zenon_frauds.TRANSACTIONS
                (step, `type`, amount, name_origin, old_balance_origin, new_balance_origin, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud)
                VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, transaction.step());
            preparedStatement.setString(2, transaction.type().name());
            preparedStatement.setBigDecimal(3, transaction.amount());
            preparedStatement.setString(4, transaction.origin().name());
            preparedStatement.setBigDecimal(5, transaction.origin().oldBalance());
            preparedStatement.setBigDecimal(6, transaction.origin().newBalance());
            preparedStatement.setString(7, transaction.recipient().name());
            preparedStatement.setBigDecimal(8, transaction.recipient().oldBalance());
            preparedStatement.setBigDecimal(9, transaction.recipient().newBalance());
            preparedStatement.setBoolean(10, transaction.isFraud());
            preparedStatement.setBoolean(11, transaction.isFlaggedFraud());

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving transaction.", e);
        }

    }

    private Optional<Transaction> mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        return Optional.of(new Transaction(
                resultSet.getInt("step"),
                TypeTransactionEnum.valueOf(resultSet.getString("type")),
                resultSet.getBigDecimal("amount"),
                new Customer(resultSet.getString("name_origin"), resultSet.getBigDecimal("old_balance_origin"), resultSet.getBigDecimal("new_balance_origin")),
                new Customer(resultSet.getString("name_dest"), resultSet.getBigDecimal("old_balance_dest"), resultSet.getBigDecimal("new_balance_dest")),
                resultSet.getBoolean("is_fraud"),
                resultSet.getBoolean("is_flagged_fraud")
        ));
    }
}
