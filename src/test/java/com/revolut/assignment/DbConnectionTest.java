package com.revolut.assignment;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

public class DbConnectionTest {
    @BeforeClass
    public static void setUp() {
        App.main(new String []{});
    }

    @Test
    public void connectionTest() {
        try { //jdbc:h2:tcp://localhost:9092/default
            //jdbc:h2:mem:test; jdbc:h2:./data/db
            Connection conn = DriverManager.getConnection("jdbc:h2:~/h2/AccountApi", "sa", "");
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); // check it
            PreparedStatement createTable = conn.prepareStatement(
                    "CREATE TABLE table_name (" +
                            "number_column NUMBER," +
                            "text_column VARCHAR ," +
                            "uuid_column UUID," +
                            "timestamp_column TIMESTAMP " +
                            ");");
            createTable.executeUpdate();

            PreparedStatement insertToTable = conn.prepareStatement(
                    "INSERT INTO table_name (text_column)" +
                            "VALUES ('hello');"
            );
            insertToTable.executeUpdate();

            conn.commit();
            conn.rollback();
            PreparedStatement selectFromTable = conn.prepareStatement(
                    "SELECT text_column " +
                            "FROM table_name"
            );

            ResultSet rs = selectFromTable.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1));
            }

//            String sql = "INSERT INTO employees VALUES(100, 'Smith') RETURNING id INTO ?";
//            CallableStatement callStmt = connectin.prepareCall(sql);
//            callStmt.registerOutParameter(1, Types.INTEGER);
//            int updateCnt = callStmt.executeUpdate();
//            int newId = callStmt.getInt(1);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void jdbcTransactionTest() throws SQLException {
        Connection connection = getConnection();

        try {
            connection.setAutoCommit(false);
            //select amount from account 1
            // check is money enough
            // update money counter
            // for test: throw exception if system time is devide by 2
            //select amount from account 2
            // update amount of account 2
            // write the transaction log
            connection.commit();


        } catch (Exception e){
            connection.rollback();
        }finally{
            if (connection != null) connection.close();
        }

    }

    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    @AfterClass
    public static void cleanUp() {
    }
}
