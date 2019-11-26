package mynanohttpd.example;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.io.IOException;

// TODO https://github.com/NanoHttpd/nanohttpd/wiki/Example:-Using-a-ThreadPool

public class MultipleRoutesExample extends RouterNanoHTTPD {
        public MultipleRoutesExample() throws IOException {
            super(8080);
            addMappings();
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        }

    public static void main(String[] args) throws IOException {
        new MultipleRoutesExample();
    }

        @Override
        public void addMappings() {
            // todo fill in the routes
            addRoute("/move", IndexHandler.class); // inside addMappings method
            addRoute("/user", UserHandler.class);
            addRoute("/store", StoreHandler.class);
        }
    }