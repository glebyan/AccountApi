package com.revolut.assignment;

import com.revolut.assignment.services.CreateAccountService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.UUID;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class ServicesTest {
    @BeforeClass
    public static void init (){
        App.main(new String[]{});
    }

    @Test
    public void CreateAccountServiceTest(){
        CreateAccountService service = new CreateAccountService();
        UUID uuid = null;
        SQLException res = null;
        try {
            uuid = service.createAccount();
        } catch (SQLException e) {
            e.printStackTrace();
            res = e;
        }
        System.out.println(uuid);

        assertNotNull("UUID", uuid);
        assertNull("SQLException", res);
    }


    @AfterClass
    public static void clenup(){

    }

}
