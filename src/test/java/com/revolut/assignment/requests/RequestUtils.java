package com.revolut.assignment.requests;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.UUID;

public class RequestUtils {

    private RequestUtils() {
    }

    final static String url = "http://localhost:8080";

    public static UUID createAccountReq() throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url + "/create");

        HttpResponse response = client.execute(request);
        JSONObject responseData = new JSONObject(EntityUtils.toString(response.getEntity()));
        UUID uuid = UUID.fromString(responseData.getString("UUID"));

        return uuid;
    }

    public static int depositAccountReq(UUID uuid, BigDecimal amount) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url + "/deposit");

        StringEntity params = new StringEntity(
                new JSONObject()
                        .put("UUID", uuid.toString())
                        .put("amount", amount.toString())
                        .toString()
        );
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        HttpResponse response = client.execute(request);

        return response.getStatusLine().getStatusCode();
    }

    public static BigDecimal getAccountAmountReq(UUID uuid) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url + "/total");

        StringEntity params = new StringEntity(
                new JSONObject()
                        .put("UUID", uuid.toString())
                        .toString()
        );
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        HttpResponse response = client.execute(request);

        JSONObject responseData = new JSONObject(EntityUtils.toString(response.getEntity()));

        return new BigDecimal(responseData.getString("amount")).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static int transferMoneyReq(UUID from, UUID to, BigDecimal amount) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url + "/transfer");

        StringEntity params = new StringEntity(
                new JSONObject()
                        .put("from", from.toString())
                        .put("to", to.toString())
                        .put("amount", amount.toString())
                        .toString()
        );
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        HttpResponse response = client.execute(request);

        return response.getStatusLine().getStatusCode();
    }
}
