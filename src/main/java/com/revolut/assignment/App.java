package com.revolut.assignment;

import com.revolut.assignment.services.*;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;


import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App extends RouterNanoHTTPD {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    public App(int port) throws IOException {
        super(port);
    }

    public static void main(String[] args) {
        try {
            int port = 8080;
            int webThreads = 10;

            App app = new App(port);
            app.setAsyncRunner(new BoundRunner(Executors.newFixedThreadPool(webThreads)));
            app.addMappings();
            app.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            logger.info("waiting for connections on port " + port + " using a maximum of " +
                webThreads + " web threads");

        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Couldn't start server", t);
            System.exit(1);
        }
    }

    @Override
    public void addMappings() {
        addRoute("/account", Example.class);

        addRoute("/create", CreateAccount.class); // () return UUID, 201 Created
        addRoute("/deposit", DepositAccount.class);      // (UUID, value) return 202 Accepted, or 404 Not found
        addRoute("/total", TotalAccount.class);        // (UUID) return BigDecimal, 200 OK, or 404 Not found
        addRoute("/list", ListAccounts.class );     // () return List<UUID>, 200 OK
        addRoute("/history", HistoryAccount.class); // (UUID) return List<History> 200 OK, or 404 Not found
        addRoute("/transfer", Transfer.class);      // (UUID from, UUID to, amount) 202 Accepted, or 404 Nof found, 406 Not acceptable
    }
}