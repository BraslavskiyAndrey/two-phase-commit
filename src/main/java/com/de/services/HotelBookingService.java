package com.de.services;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class HotelBookingService {

    private static final String INSERT_IN_TRANSACTION = "BEGIN; INSERT INTO hotel_booking (client_name, hotel_name, " +
            "arrival, departure) VALUES (?,?,?,?);";
    private static final String PREPARE_TRANSACTION = "PREPARE TRANSACTION '%s';";
    private static final String COMMIT_TRANSACTION = "COMMIT PREPARED '%s';";
    private static final String ROLLBACK_TRANSACTION = "ROLLBACK PREPARED '%s';";
    private static final String TRANSACTION_NAME_PREFIX = "Hotel-";

    private final DataSource dataSource;

    public HotelBookingService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void bookHotel(String transactionId) {
        final String transactionQuery = INSERT_IN_TRANSACTION + String.format(PREPARE_TRANSACTION,
                createTransactionId(transactionId));
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(transactionQuery)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, "Andrii-" + transactionId);
            preparedStatement.setString(2, "Hotel-" + transactionId);
            preparedStatement.setDate(3, new java.sql.Date(1606780800000L));
            preparedStatement.setDate(4, new java.sql.Date(1606780800000L));
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to book hotel");
        }
    }

    public void commitBooking(String transactionId) {
        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement();) {
            statement.execute(String.format(COMMIT_TRANSACTION, createTransactionId(transactionId)));
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to commit");
        }
    }

    public void rollbackBooking(String transactionId) {
        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement();) {
            statement.execute(String.format(ROLLBACK_TRANSACTION, createTransactionId(transactionId)));
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to rollback");
        }
    }

    private String createTransactionId(String transactionId) {
        return TRANSACTION_NAME_PREFIX + transactionId;
    }
}
