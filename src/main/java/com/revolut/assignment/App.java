package com.revolut.assignment;

import com.revolut.assignment.controllers.*;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;


import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.revolut.assignment.utils.Utils.dbInit;


public class App extends RouterNanoHTTPD {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    public App(int port) throws IOException {
        super(port);
//        startH2();
//        dbInit();
    }

    public static void main(String[] args) {
        try {
            int port = 8080;
            int webThreads = 10;

            App app = new App(port);
            app.setAsyncRunner(new BoundRunner(Executors.newFixedThreadPool(webThreads)));
            app.addMappings();
            app.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            logger.info("WEB server started on port " + port + " (" +
                    webThreads + " threads)");

        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Couldn't start server", t);
            System.exit(1);
        }
    }

    public static void exit() {
        System.exit(0);
    }

    @Override
    public void addMappings() {

        addRoute("/create", CreateAccountController.class); // () return UUID, 201 Created
        addRoute("/deposit", DepositAccountController.class);      // (UUID, value) return 202 Accepted, or 404 Not found
        addRoute("/total", GetAccountAmountController.class);        // (UUID) return BigDecimal, 200 OK, or 404 Not found
//        addRoute("/list", GetAccountsListController.class);     // () return List<UUID>, 200 OK
//        addRoute("/history", GetAccountHistoryController.class); // (UUID) return List<History> 200 OK, or 404 Not found
        addRoute("/transfer", TransferMoneyController.class);      // (UUID from, UUID to, amount) 202 Accepted, or 404 Nof found, 406 Not acceptable
    }

//    private static void startH2() {
//        try {
//            Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
//            logger.info("H2 Server started");
//            Utils.dbInit();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
}