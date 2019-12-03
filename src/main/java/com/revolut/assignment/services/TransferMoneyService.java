package com.revolut.assignment.services;

import com.revolut.assignment.exceptions.AccountNotExistException;
import com.revolut.assignment.exceptions.NotEnoughMoneyException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

public class TransferMoneyService {

    Connection connection;

    private static final Logger logger = Logger.getLogger(TransferMoneyService.class.getName());

    public void transferMoney(UUID from, UUID to, BigDecimal delta) throws SQLException {

        try {
            delta.setScale(2, RoundingMode.HALF_EVEN);
            connection = DriverManager.getConnection(
                    "jdbc:h2:~/h2/AccountApi", "sa", "");
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            PreparedStatement checkIfAccountFromExist = connection.prepareStatement(
                    "select case when count(*) > 0 then true else false end from accounts where ACCOUNT_UUID = ? "
            );
            checkIfAccountFromExist.setString(1, from.toString());
            checkIfAccountFromExist.executeQuery();
            ResultSet checkIfAccountFromExistResultSet = checkIfAccountFromExist.getResultSet();
            checkIfAccountFromExistResultSet.next();
            if (!checkIfAccountFromExistResultSet.getBoolean(1)){
                connection.rollback();
                connection.close();
                throw new AccountNotExistException("Account not exist", new NullPointerException());
            }

            PreparedStatement checkIfAccountToExist = connection.prepareStatement(
                    "select case when count(*) > 0 then true else false end from accounts where ACCOUNT_UUID = ? "
            );
            checkIfAccountToExist.setString(1, to.toString());
            checkIfAccountToExist.executeQuery();
            ResultSet checkIfAccountToExistResultSet = checkIfAccountToExist.getResultSet();
            checkIfAccountToExistResultSet.next();
            if (!checkIfAccountToExistResultSet.getBoolean(1)){
                connection.rollback();
                connection.close();
                throw new AccountNotExistException("Account not exist", new NullPointerException());
            }

            PreparedStatement selectCurrentAmount = connection.prepareStatement(
                    "select TOTAL_AMOUNT from accounts where ACCOUNT_UUID = ? ;"
            );

            selectCurrentAmount.setString(1, from.toString());
            selectCurrentAmount.executeQuery();
            ResultSet selectCurrentAmountResultSet = selectCurrentAmount.getResultSet();
            selectCurrentAmountResultSet.next();
            BigDecimal currentAccountAmount = selectCurrentAmountResultSet.getBigDecimal(1);
            currentAccountAmount.setScale(2, RoundingMode.HALF_EVEN);
            if (currentAccountAmount.compareTo(delta) == -1){
                connection.rollback();
                connection.close();
                throw new NotEnoughMoneyException("Not enough money");
            }

            PreparedStatement updateAccountFromAmount = connection.prepareStatement(
                    "update accounts set TOTAL_AMOUNT = ? where ACCOUNT_UUID = ?"
            );
            updateAccountFromAmount.setBigDecimal(1, currentAccountAmount.subtract(delta));
            updateAccountFromAmount.setString(2, from.toString());
            updateAccountFromAmount.executeUpdate();

            PreparedStatement selectToAmount = connection.prepareStatement(
                    "select TOTAL_AMOUNT from accounts where ACCOUNT_UUID = ? ;"
            );

            selectToAmount.setString(1, to.toString());
            selectToAmount.executeQuery();
            ResultSet selectToAmountRs = selectToAmount.getResultSet();
            selectToAmountRs.next();
            BigDecimal toAccountCurrentAmount = selectToAmountRs.getBigDecimal(1);
            toAccountCurrentAmount.setScale(2, RoundingMode.HALF_EVEN);


            PreparedStatement updateAccountToAmount = connection.prepareStatement(
                    "update accounts set TOTAL_AMOUNT = ? where ACCOUNT_UUID = ? "
            );
            updateAccountToAmount.setBigDecimal(1, toAccountCurrentAmount.add(delta));
            updateAccountToAmount.setString(2, to.toString());

            updateAccountToAmount.executeUpdate();

            connection.commit();
        }catch(SQLException e){
            throw e;
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e){
                logger.warning("Connection is not closed properly");
            }
        }
    }
}
