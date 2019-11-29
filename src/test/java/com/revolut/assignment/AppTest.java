package com.revolut.assignment;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.*;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AppTest {


    @BeforeClass
    public static void setUp() {
        App.main(new String[]{});

    }

    @Test
    public void createAccountsTest() throws Exception {

        for (int i = 0; i < 100; i++) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            UUID uuid;
            StringWriter writer = new StringWriter();

            try {

                HttpPost postRequest = new HttpPost("http://localhost:8080/create");
                postRequest.addHeader("content-type", "application/json");
                HttpResponse response = httpClient.execute(postRequest);
                String responseJSON = EntityUtils.toString(response.getEntity());

                int statusCode = response.getStatusLine().getStatusCode();
//                System.out.println(response.getEntity().getContentType());
//                System.out.println(responseJSON);

                JSONObject jsonObject = new JSONObject(responseJSON.toString());
                uuid = UUID.fromString(jsonObject.getString("UUID"));
                System.out.println("The UUID = " + uuid);

                assertEquals(statusCode, 201);

            } finally {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }

    @Test
    public void depositAccountsTest() throws Exception {

        List<UUID> accountUUIDList = createAccounts();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                for (UUID u : accountUUIDList) {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    StringWriter writer = new StringWriter();
                    try {
                        HttpPost postRequest = new HttpPost("http://localhost:8080/deposit");

                        postRequest.addHeader("content-type", "application/json");

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("UUID", u.toString());
                        jsonObject.put("amount", "1000");
                        writer.append(jsonObject.toString());

                        StringEntity userEntity = null;

                        userEntity = new StringEntity(writer.getBuffer().toString());

                        postRequest.setEntity(userEntity);

                        HttpResponse response = httpClient.execute(postRequest);

                        String responseJSON = EntityUtils.toString(response.getEntity());

                        int statusCode = response.getStatusLine().getStatusCode();
                        System.out.println(response.getEntity().getContentType());
                        System.out.println(responseJSON);

                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        httpClient.getConnectionManager().shutdown();
                    }
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(run).start();
        }
        Thread.currentThread().sleep(10_000);
        for (UUID u :accountUUIDList){
            DefaultHttpClient httpClient = new DefaultHttpClient();
            try {

                HttpPost postRequest = new HttpPost("http://localhost:8080/total");
                postRequest.addHeader("content-type", "application/json");

                JSONObject jsonRequest = new JSONObject().put("UUID", u.toString());

                StringEntity entity = new StringEntity(jsonRequest.toString());
                System.out.println(jsonRequest);
                postRequest.setEntity(entity);

                HttpResponse response = httpClient.execute(postRequest);
                String responseJSON = EntityUtils.toString(response.getEntity());

                JSONObject jsonResponse = new JSONObject(responseJSON);

                BigInteger total = new BigInteger(jsonResponse.get("total").toString());

                assertEquals(jsonRequest.toString(), total, new BigInteger("10000"));

            } finally {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }

    @Test
    @Ignore
    public void moneyTransferTest() throws Exception {

        List <UUID> list = deposit();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 99; i ++) {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    StringWriter writer = new StringWriter();
                    try {
                        HttpPost postRequest = new HttpPost("http://localhost:8080/transfer");

                        postRequest.addHeader("content-type", "application/json");

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("from", list.get(i).toString());
                        jsonObject.put("to", list.get(i+1).toString());
                        jsonObject.put("delta", "1000");
                        writer.append(jsonObject.toString());

                        StringEntity userEntity = null;

                        userEntity = new StringEntity(writer.getBuffer().toString());

                        postRequest.setEntity(userEntity);

                        HttpResponse response = httpClient.execute(postRequest);

                        String responseJSON = EntityUtils.toString(response.getEntity());

                        int statusCode = response.getStatusLine().getStatusCode();
                        System.out.println(response.getEntity().getContentType());
                        System.out.println(responseJSON);

                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        httpClient.getConnectionManager().shutdown();
                    }
                }
            }
        };


        List <Thread> threads= new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(run);
            threads.add(thread);
            thread.start();
        }

        threads.stream().forEach(p -> {
            try {
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(getAccountAmount(list.get(0)));
        System.out.println(getAccountAmount(list.get(99)));




    }



    private static List<UUID> createAccounts() throws IOException {
        List<UUID> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            UUID uuid;
            try {

                HttpPost postRequest = new HttpPost("http://localhost:8080/create");
                postRequest.addHeader("content-type", "application/json");
                HttpResponse response = httpClient.execute(postRequest);
                String responseJSON = EntityUtils.toString(response.getEntity());

                int statusCode = response.getStatusLine().getStatusCode();

                JSONObject jsonObject = new JSONObject(responseJSON.toString());
                uuid = UUID.fromString(jsonObject.getString("UUID"));
                list.add(uuid);

            } finally {
                httpClient.getConnectionManager().shutdown();
            }
        }
        return list;
    }

    public static List <UUID> deposit() throws IOException {
        List <UUID> list = createAccounts();

        for (UUID u : list) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            StringWriter writer = new StringWriter();
            try {
                HttpPost postRequest = new HttpPost("http://localhost:8080/deposit");

                postRequest.addHeader("content-type", "application/json");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("UUID", u.toString());
                jsonObject.put("amount", "1000");
                writer.append(jsonObject.toString());

                StringEntity userEntity = null;

                userEntity = new StringEntity(writer.getBuffer().toString());

                postRequest.setEntity(userEntity);

                HttpResponse response = httpClient.execute(postRequest);

//                String responseJSON = EntityUtils.toString(response.getEntity()); //
//
//                int statusCode = response.getStatusLine().getStatusCode();


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpClient.getConnectionManager().shutdown();
            }
        }
    return list;
    }


    public static BigDecimal getAccountAmount(UUID uuid){

        String value = null;

            DefaultHttpClient httpClient = new DefaultHttpClient();

            try {

                HttpPost postRequest = new HttpPost("http://localhost:8080/total");
                postRequest.addHeader("content-type", "application/json");




                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("UUID", uuid);





                StringEntity userEntity = null;

                userEntity = new StringEntity(jsonRequest.toString());

                postRequest.setEntity(userEntity);


                HttpResponse response = httpClient.execute(postRequest);
                String responseJSON = EntityUtils.toString(response.getEntity());

                JSONObject jsonObject = new JSONObject(responseJSON.toString());
                value = jsonObject.getString("amount");


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpClient.getConnectionManager().shutdown();
            }


        return new BigDecimal(value);
    }


}