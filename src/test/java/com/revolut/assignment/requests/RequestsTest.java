package com.revolut.assignment.requests;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

public class RequestsTest {

    @Test
    public void requestsTest(){

        try {


            UUID uuid = RequestUtils.createAccountReq();
            System.out.println("Creating account : " + uuid);

            System.out.println("Deposit 1000 status : " + RequestUtils.depositAccountReq(uuid, new BigDecimal(1000)));
            for (int i = 0; i < 10; i++){
                RequestUtils.depositAccountReq(uuid, new BigDecimal(1.24));
            }

            System.out.println(uuid + " : " +RequestUtils.getAccountAmount(uuid));

            for (int i = 0; i < 10; i++){
                RequestUtils.createAccountReq();
            }

            System.out.println(RequestUtils.getFullAccountsList());

            System.out.println(RequestUtils.getAccountHistoryReq(uuid));



        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
