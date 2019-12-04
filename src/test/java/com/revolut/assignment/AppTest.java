package com.revolut.assignment;

import org.junit.*;
import org.junit.runners.MethodSorters;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.revolut.assignment.requests.RequestUtils.*;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    int numberOfAccounts = 10; //should be divisible by 10 without remainder

    @BeforeClass
    public static void setUp() {
        App.main(new String[]{});
    }

    @Test
    public void test1CreateAccount() throws SQLException {
        try {


            for (int i = 0; i < numberOfAccounts; i++) {
                createAccountReq();
            }

            Connection connection = DriverManager.getConnection("jdbc:h2:~/h2/AccountApi", "sa", "");

            PreparedStatement ps = connection.prepareStatement(
                    "select count(*) from ACCOUNTS"
            );

            ps.executeQuery();
            ResultSet resultSet = ps.getResultSet();
            resultSet.next();
            Long quantity = resultSet.getLong(1);

            assertEquals(new Long(numberOfAccounts), quantity);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2DepositAccount() throws SQLException {
        try {

            ArrayList<UUID> accounts = new ArrayList();

            Connection connection = DriverManager.getConnection("jdbc:h2:~/h2/AccountApi", "sa", "");

            PreparedStatement selectAccountUuidFromAccounts = connection.prepareStatement(
                    "select ACCOUNT_UUID from ACCOUNTS"
            );

            selectAccountUuidFromAccounts.executeQuery();
            ResultSet selectAccountUuidFromAccountsResultSet = selectAccountUuidFromAccounts.getResultSet();
            while (selectAccountUuidFromAccountsResultSet.next()) {
                accounts.add(UUID.fromString(selectAccountUuidFromAccountsResultSet.getString(1)));
            }

            for (UUID u : accounts){
                depositAccountReq(u, new BigDecimal(10_000).setScale(2, RoundingMode.HALF_EVEN));
            }

            for (UUID u : accounts){
                assertEquals(new BigDecimal(10_000).setScale(2, RoundingMode.HALF_EVEN),
                        getAccountAmountReq(u));

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3TransferMoney() throws SQLException, IOException, InterruptedException {
        ArrayList<UUID> accounts = new ArrayList();

        Connection connection = DriverManager.getConnection("jdbc:h2:~/h2/AccountApi", "sa", "");

        PreparedStatement selectAccountUuidFromAccounts = connection.prepareStatement(
                "select ACCOUNT_UUID from ACCOUNTS"
        );

        selectAccountUuidFromAccounts.executeQuery();
        ResultSet selectAccountUuidFromAccountsResultSet = selectAccountUuidFromAccounts.getResultSet();
        while (selectAccountUuidFromAccountsResultSet.next()) {
            accounts.add(UUID.fromString(selectAccountUuidFromAccountsResultSet.getString(1)));
        }

        Runnable run = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < accounts.size()-1; i++){
                    try {
                        transferMoneyReq(accounts.get(i), accounts.get(i+1),
                                new BigDecimal(1000).setScale(2, RoundingMode.HALF_EVEN));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            threadList.add(new Thread(run));
        }
        for (Thread t : threadList){
            t.start();
            t.join();
        }

        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN),
                getAccountAmountReq(accounts.get(0)));
        assertEquals(new BigDecimal(10_000).setScale(2, RoundingMode.HALF_EVEN),
                getAccountAmountReq(accounts.get(1)));
        assertEquals(new BigDecimal(10_000).setScale(2, RoundingMode.HALF_EVEN),
                getAccountAmountReq(accounts.get(numberOfAccounts-2)));
        assertEquals(new BigDecimal(20_000).setScale(2, RoundingMode.HALF_EVEN),
                getAccountAmountReq(accounts.get(numberOfAccounts-1)));


    }


}