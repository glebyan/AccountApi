package com.revolut.assignment;

import com.revolut.assignment.exceptions.AccountNotExistException;
import com.revolut.assignment.exceptions.NotEnoughMoneyException;
import com.revolut.assignment.services.CreateAccountService;
import com.revolut.assignment.services.DepositAccountService;
import com.revolut.assignment.services.GetAccountAmountService;
import com.revolut.assignment.services.TransferMoneyService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.UUID;

import static com.revolut.assignment.utils.Utils.dbInit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class ServicesTest {

    @BeforeClass
    public static void setUpDB(){
        dbInit();
    }

    @Test
    public void CreateAccountServiceTest() {
        CreateAccountService service = new CreateAccountService();
        UUID uuid = null;
        SQLException res = null;
        try {
            uuid = service.createAccount();
        } catch (SQLException e) {
            e.printStackTrace();
            res = e;
        }

        assertNotNull("UUID", uuid);
        assertNull("SQLException", res);
    }

    @Test
    public void DepositAccountServicePositiveTest() throws SQLException {
        CreateAccountService service = new CreateAccountService();
        UUID uuid = null;

        uuid = service.createAccount();

        DepositAccountService depositAccountService = new DepositAccountService();
        depositAccountService.depositAccount(uuid, new BigDecimal(1000));

        Connection connection = DriverManager.getConnection("jdbc:h2:~/h2/AccountApi", "sa", "");

        PreparedStatement ps = connection.prepareStatement(
                "select TOTAL_AMOUNT from ACCOUNTS where ACCOUNT_UUID = ?"
        );

        ps.setString(1, uuid.toString());
        ps.executeQuery();
        ResultSet resultSet = ps.getResultSet();
        resultSet.next();
        BigDecimal amount = resultSet.getBigDecimal(1);

        assertEquals(amount, new BigDecimal(1000).setScale(2, RoundingMode.HALF_EVEN));

    }

    @Test
    public void DepositAccountServiceNegativeTest() throws SQLException {

        UUID uuid = UUID.randomUUID();

        AccountNotExistException t = null;

        DepositAccountService depositAccountService = new DepositAccountService();
        try {
            depositAccountService.depositAccount(uuid, new BigDecimal(1000));
        } catch(AccountNotExistException e){
            t = e;
        }

        assertNotNull(t);
    }

    @Test
    public void GetAccountAmountServicePositiveTest() throws SQLException {
        CreateAccountService service = new CreateAccountService();
        UUID uuid = service.createAccount();

        DepositAccountService depositAccountService = new DepositAccountService();
        depositAccountService.depositAccount(uuid, new BigDecimal(1000));

        GetAccountAmountService getAccountAmountService = new GetAccountAmountService();
        BigDecimal amount = getAccountAmountService.getAmount(uuid);

        assertEquals(amount, new BigDecimal(1000).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    public void GetAccountAmountServiceNegativeTest() throws SQLException {
        UUID uuid = UUID.randomUUID();

        GetAccountAmountService getAccountAmountService = new GetAccountAmountService();

        AccountNotExistException t = null;

        try{
            getAccountAmountService.getAmount(uuid);
        } catch(AccountNotExistException e){
            t = e;
        }
    }

    @Test
    public void TransferMoneyServicePositiveTest() throws SQLException {
        TransferMoneyService transferMoneyService = new TransferMoneyService();
        CreateAccountService createAccountService = new CreateAccountService();
        DepositAccountService depositAccountService = new DepositAccountService();

        UUID from = createAccountService.createAccount();
        UUID to = createAccountService.createAccount();

        depositAccountService.depositAccount(from, new BigDecimal(1000).setScale(2, RoundingMode.HALF_EVEN));

        transferMoneyService.transferMoney(from, to, new BigDecimal(500).setScale(2, RoundingMode.HALF_EVEN));

        Connection connection = DriverManager.getConnection("jdbc:h2:~/h2/AccountApi", "sa", "");

        PreparedStatement selectFromSt = connection.prepareStatement(
                "select TOTAL_AMOUNT from ACCOUNTS where ACCOUNT_UUID = ?"
        );

        selectFromSt.setString(1, from.toString());
        selectFromSt.executeQuery();
        ResultSet selectFromStResultSet = selectFromSt.getResultSet();
        selectFromStResultSet.next();
        BigDecimal fromAmount = selectFromStResultSet.getBigDecimal(1);
        fromAmount.setScale(2, RoundingMode.HALF_EVEN);

        PreparedStatement selectToSt = connection.prepareStatement(
                "select TOTAL_AMOUNT from ACCOUNTS where ACCOUNT_UUID = ?"
        );

        selectToSt.setString(1, to.toString());
        selectToSt.executeQuery();
        ResultSet selectToRs = selectToSt.getResultSet();
        selectToRs.next();
        BigDecimal toAmount = selectToRs.getBigDecimal(1);

        assertEquals("from value", new BigDecimal(500).setScale(2, RoundingMode.HALF_EVEN), fromAmount);
        assertEquals("to value ", new BigDecimal(500).setScale(2, RoundingMode.HALF_EVEN), toAmount);
    }

    @Test
    public void TransferMoneyServiceNoEnoughMoneyTest() throws SQLException {
        TransferMoneyService transferMoneyService = new TransferMoneyService();
        CreateAccountService createAccountService = new CreateAccountService();

        UUID from = createAccountService.createAccount();
        UUID to = createAccountService.createAccount();

        NotEnoughMoneyException notEnoughMoneyException = null;

        try {
            transferMoneyService.transferMoney(from, to, new BigDecimal(500).setScale(2, RoundingMode.HALF_EVEN));
        }catch(NotEnoughMoneyException e){
            notEnoughMoneyException = e;
        }

        assertNotNull(notEnoughMoneyException);
    }

    @Test
    public void TransferMoneyServiceAccountFromNotExistedTest() throws SQLException {
        TransferMoneyService transferMoneyService = new TransferMoneyService();
        CreateAccountService createAccountService = new CreateAccountService();

        UUID from = UUID.randomUUID();//createAccountService.createAccount();
        UUID to = createAccountService.createAccount();

        AccountNotExistException accountNotExistException = null;

        try {
            transferMoneyService.transferMoney(from, to, new BigDecimal(500).setScale(2, RoundingMode.HALF_EVEN));
        }catch(AccountNotExistException e){
            accountNotExistException = e;
        }

        assertNotNull(accountNotExistException);
    }

    @Test
    public void TransferMoneyServiceAccountToNotExistedTest() throws SQLException {
        TransferMoneyService transferMoneyService = new TransferMoneyService();
        CreateAccountService createAccountService = new CreateAccountService();

        UUID from = createAccountService.createAccount();
        UUID to = UUID.randomUUID();//createAccountService.createAccount();

        AccountNotExistException accountNotExistException = null;

        try {
            transferMoneyService.transferMoney(from, to, new BigDecimal(500).setScale(2, RoundingMode.HALF_EVEN));
        }catch(AccountNotExistException e){
            accountNotExistException = e;
        }

        assertNotNull(accountNotExistException);
    }

    @AfterClass
    public static void cleanup() {

    }

}
