package mynonohttpd.pool.example;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import mynanohttpd.example.StoreHandler;
import mynanohttpd.example.UserHandler;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


/** App is a simple little web server with some constraints on the number of threads. */
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
        // todo fill in the routes
        addRoute("/move", RouterNanoHTTPD.IndexHandler.class); // inside addMappings method
        addRoute("/user", UserHandler.class);
        addRoute("/store", StoreHandler.class);
    }



}