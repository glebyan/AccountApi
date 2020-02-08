package com.revolut.assignment.services;

import com.revolut.assignment.exceptions.AccountNotExistException;
import com.revolut.assignment.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

public class DepositAccountService {

    Connection connection;

    private static final Logger logger = Logger.getLogger(DepositAccountService.class.getName());

    public void depositAccount(UUID uuid, BigDecimal delta) throws SQLException {

        try {
            connection = Utils.getConnection();

            PreparedStatement checkIfAccountExist = connection.prepareStatement(
                    "select count(*) from accounts where ACCOUNT_UUID = ? "
            );
            checkIfAccountExist.setString(1, uuid.toString());
            checkIfAccountExist.executeQuery();
            ResultSet checkIfAccountExistResultSet = checkIfAccountExist.getResultSet();
            checkIfAccountExistResultSet.next();
            if (checkIfAccountExistResultSet.getInt(1) == 0 ){
                connection.rollback();
                connection.close();
                throw new AccountNotExistException("Account not exist", new NullPointerException());

            }

            PreparedStatement selectCurrentAmount = connection.prepareStatement(
                    "select TOTAL_AMOUNT from accounts where ACCOUNT_UUID = ? "
            );

            selectCurrentAmount.setString(1, uuid.toString());
            selectCurrentAmount.executeQuery();
            ResultSet selectCurrentAmountResultSet = selectCurrentAmount.getResultSet();
            selectCurrentAmountResultSet.next();
            BigDecimal currentAccountAmount = selectCurrentAmountResultSet.getBigDecimal(1);

            currentAccountAmount.setScale(2, RoundingMode.HALF_EVEN);

            BigDecimal newAccountAmount = currentAccountAmount.add(delta).setScale(2, RoundingMode.HALF_EVEN);

            PreparedStatement updateAccountAmount = connection.prepareStatement(
                    "update accounts set TOTAL_AMOUNT = ? where ACCOUNT_UUID = ? "
            );
            updateAccountAmount.setBigDecimal(1, newAccountAmount);
            updateAccountAmount.setString(2, uuid.toString());

            updateAccountAmount.executeUpdate();

            connection.commit();
        }catch(SQLException e){
            System.out.println(e);
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
