package com.revolut.assignment.services;

import com.revolut.assignment.App;
import com.revolut.assignment.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class CreateAccountService {
    Connection connection;

    private static final Logger logger = Logger.getLogger(CreateAccountService.class.getName());

    public UUID createAccount() throws SQLException {

        try {
            connection = Utils.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            PreparedStatement ps = connection.prepareStatement(
                    "insert into  accounts (account_uuid, total_amount) " +
                            "VALUES (?,?)"
            );

            UUID uuid = UUID.randomUUID();
            BigDecimal amount = new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);

            ps.setString(1, uuid.toString());
            ps.setBigDecimal(2, amount);

            ps.executeUpdate();

            connection.commit();

            return uuid;
        }catch(SQLException e){
            System.out.println(e);
            throw e;
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e){
                logger.warning("Connection is not closed");
            }
        }
    }
}
