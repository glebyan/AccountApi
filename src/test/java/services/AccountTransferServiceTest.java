package services;

import com.revolut.assignment.datamodel.Account;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountTransferServiceTest {

    TreeMap<Integer, Account> sortedMap = new TreeMap();
    Map datastore = Collections.synchronizedSortedMap(sortedMap);
    List <UUID> accountNumberList = Collections.synchronizedList(new ArrayList<>());

    // нужно заполнить датастор

    @Test
    public void moneyTransferTest(){
        /* создаем 1000 аккаунтов
         кладем на первый аккаунт в листе 10_000 уе
         запускам в 10 потоков
         цикличный перевод по всем аккаунтам с первого по последний по 1000 рублей63
         все должно отработать, на последнем аккаунте должно оказаться 10_000 уе
        */
    }

    @Test
    public void AccountCreationTest() throws InterruptedException {
        datastore.clear();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.execute(new AccountAddWorker());
        }
        Thread.currentThread().sleep(1000); // нужно подцепиться к потокам которые выполняются по уму
        executorService.shutdown();

        assertEquals("Size of datastore ", datastore.size(), 1000 );
        assertEquals("Size of accountNumberList ", accountNumberList.size(), 1000);

        // коряво, но тест проходит

        datastore.clear();

    }

    class AccountAddWorker implements Runnable{
        @Override
        public void run(){
            for (int i = 0; i < 100; i++){
                UUID uuid = UUID.randomUUID();
                datastore.put(uuid, new Account());
                accountNumberList.add(uuid);
            }
        }
    }


}