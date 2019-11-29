package com.revolut.assignment.storage;

import com.revolut.assignment.datamodel.Account;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class InMemoryTest {

    @Before
    public void fillDatabase(){
        for (int i = 0; i < 100; i ++) {
            BigDecimal bigDecimal = new BigDecimal(new Random().nextInt() + 5.22);

            System.out.println(
                    InMemory.getAccountByUUID(InMemory.crateAccount()).changeValue(new BigDecimal(
                            new Random().nextInt() + 5.22))
            );
        }
    }

    @Test
    public void getAccoutByUUIDTest(){
        int counter = 0;
        for (Account a : InMemory.getAllAccounts()){
            System.out.println(a);
            counter++;
        }
        assertEquals("Number of accounts", 100, counter);
    }
}
