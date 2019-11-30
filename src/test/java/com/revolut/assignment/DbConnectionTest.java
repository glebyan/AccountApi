package com.revolut.assignment;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

public class DbConnectionTest {
    @BeforeClass
    public static void setUp() {
        try {
            Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
            System.out.println("Server started");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void connectionTest() {
        try { //jdbc:h2:mem:test; jdbc:h2:./data/db
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
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

//            conn.commit();
            conn.rollback();
            PreparedStatement selectFromTable = conn.prepareStatement(
                    "SELECT text_column " +
                            "FROM table_name"
            );

            ResultSet rs = selectFromTable.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1));
            }




        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void cleanUp() {
        try {
            Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
