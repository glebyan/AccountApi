package com.revolut.assignment.services;

import com.revolut.assignment.AccountNotExistException;
import com.revolut.assignment.App;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

public class DepositAccountService {

    Connection connection;

    private static final Logger logger = Logger.getLogger(App.class.getName());

    public void depositAccount(UUID uuid, BigDecimal delta) throws SQLException {

        try {
            connection = DriverManager.getConnection(
                    "jdbc:h2:~/h2/AccountApi", "sa", "");
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            PreparedStatement checkIfAccountExist = connection.prepareStatement(
                    "select count from ACCOUNTS where ACCOUNT_UUID = ? "
            );
            checkIfAccountExist.executeQuery();
            ResultSet checkIfAccountExistResultSet = checkIfAccountExist.getResultSet();
            if (checkIfAccountExistResultSet.getInt(1) == 0){
                connection.rollback();
                connection.close();
                throw new AccountNotExistException("Account not exist", new NullPointerException());

            }

            PreparedStatement selectCurrentAmount = connection.prepareStatement(
                    "select TOTAL_AMOUNT from accounts where ACCOUNT_UUID = ? ;"
            );

            selectCurrentAmount.setString(1, uuid.toString());
            selectCurrentAmount.executeQuery();
            ResultSet selectCurrentAmountResultSet = selectCurrentAmount.getResultSet();

            BigDecimal currentAccountAmount = selectCurrentAmountResultSet.getBigDecimal(1);

            currentAccountAmount.setScale(2, RoundingMode.HALF_EVEN);

            BigDecimal newAccountAmount = currentAccountAmount.add(delta).setScale(2, RoundingMode.HALF_EVEN);

            PreparedStatement updateAccountAmount = connection.prepareStatement(
                    "update accounts set TOTAL_AMOUNT = ? "
            );
            updateAccountAmount.setString(1, newAccountAmount.toString());

            updateAccountAmount.executeUpdate();

            connection.commit();
        }catch(SQLException e){
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
