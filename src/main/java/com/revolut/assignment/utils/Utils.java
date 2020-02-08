package com.revolut.assignment.utils;

import fi.iki.elonen.NanoHTTPD;
import org.h2.tools.RunScript;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    private Utils(){}

    public static final String WRONG_REQUEST = "{\"errormessage\":\"wrong request type\"}";
    public static final String ACCOUNT_NOT_EXIST = "{\"errormessage\":\"given account not exist\"}";
    public static final String NOT_ENOUGH_MONEY = "{\"errormessage\":\"not enough money\"}";

//    private static final String CONNECTION_STRUING = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String CONNECTION_STRUING = "jdbc:h2:tcp://localhost:9092/~/H2DB/accountapi";
//    private static final String PASSWORD = "revolut";
    private static final String PASSWORD = "sa";
//    private static final String USERNAME = "revolut";
    private static final String USERNAME = "sa";

    public static JSONObject getJSONObject(NanoHTTPD.IHTTPSession session){

        final HashMap<String, String> map = new HashMap<String, String>();
        try {
            session.parseBody(map);
        } catch (IOException | NanoHTTPD.ResponseException e) {
            e.printStackTrace();
        }
        final String json = map.get("postData");

        return new JSONObject(json);
    }

    public static BigDecimal getMoney(){
        return new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal getMoney(String string){
        return new BigDecimal(string).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static void dbInit(){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("schema.sql");
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:~/h2/AccountApi", "sa", "")){
            RunScript.execute(connection, reader);
            logger.info("DB successful initialized");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Couldn't initialize DB", e);
        }
    }

    public static Connection getConnection()  {

        try{
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e){
            logger.warning("cant find driver class\n" + e);
        }

        Connection connection = null;
        try {
            connection =  DriverManager.getConnection(CONNECTION_STRUING, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        logger.info("Connection returned");
        return connection;
    }
}
