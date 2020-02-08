package com.revolut.assignment;

import com.revolut.assignment.controllers.*;
import com.revolut.assignment.utils.Utils;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.h2.tools.Server;


import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.revolut.assignment.utils.Utils.dbInit;

/* GLOBAL TO DO LIST
 * TODO Растащить приложение и интеграционные тесты на два модуля в одном проекте, чтобы у каждого был собственный pom
 *  который ссылается на родительскоий pom
 * TODO настроить профили сборки в мавене:
 *  1. Сборка проекта с разными БД
 *  2. Сборка проекта со встроенными интеграционными тестами
 *  3. Сборка проекта и тестов отдельно
 * TODO добавить логгирование в тесты
 * TODO создать интерфейсы для сервисов
 * TODO вынести код общения с БД из сервисов - создать DAO
 * TODO добавить поддержку IoC (google)
 * TODO перевести на jetty
 */

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