package com.de.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountManagementService.class);

    private static final String UPDATE_BALANCE = "BEGIN; UPDATE account SET amount = amount - 100 WHERE id = 1;";
    private static final String PREPARE_TRANSACTION = "PREPARE TRANSACTION '%s';";
    private static final String COMMIT_TRANSACTION = "COMMIT PREPARED '%s';";
    private static final String ROLLBACK_TRANSACTION = "ROLLBACK PREPARED '%s';";
    private static final String TRANSACTION_NAME_PREFIX = "Account-";

    private final DataSource dataSource;

    public AccountManagementService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void withdrawAccount(String transactionId) {
        final String transactionQuery = UPDATE_BALANCE + String.format(PREPARE_TRANSACTION,
                createTransactionId(transactionId));
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(transactionQuery)) {
            connection.setAutoCommit(false);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to book withdraw");
        }
    }

    public void commitBooking(String transactionId) {
        final String accountTransactionId = createTransactionId(transactionId);
        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement();) {
            statement.execute(String.format(COMMIT_TRANSACTION, accountTransactionId));
        } catch (SQLException exception) {
            logger.warn("Failed to commit account transaction {}", accountTransactionId);
        }
    }

    public void rollbackBooking(String transactionId) {
        final String accountTransactionId = createTransactionId(transactionId);
        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement();) {
            connection.setAutoCommit(false);
            statement.execute(String.format(ROLLBACK_TRANSACTION, accountTransactionId));
            connection.commit();
        } catch (SQLException exception) {
            logger.warn("Failed to rollback account transaction {}", accountTransactionId);
        }
    }

    private String createTransactionId(String transactionId) {
        return TRANSACTION_NAME_PREFIX + transactionId;
    }
}
