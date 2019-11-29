package com.revolut.assignment.requests;

import com.revolut.assignment.datamodel.History;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.io.IOException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class RequestUtils {

    private RequestUtils() {
    }

    ;

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

    public static List<History> getAccountHistoryReq(UUID uuid) throws IOException {

        List<History> list = new LinkedList<>();

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url + "/history");

        StringEntity params = new StringEntity(
                new JSONObject()
                        .put("UUID", uuid.toString())
                        .toString()
        );
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        HttpResponse response = client.execute(request);

        JSONArray responseArray = new JSONObject(EntityUtils.toString(response.getEntity())).getJSONArray("list");
        for (int i = 0; i < responseArray.length(); i++) {

            JSONObject responseData = responseArray.getJSONObject(i);
            list.add(responseData.getInt("historyid"),
                    new History(
                            responseData.getLong("timestamp"),
                            responseData.getBigDecimal("delta").setScale(2, RoundingMode.HALF_EVEN),
                            new String(responseData.getString("comment")),
                            responseData.getBoolean("result"),
                            responseData.getBoolean("revert"),
                            responseData.getBigDecimal("currentAmount").setScale(2, RoundingMode.HALF_EVEN)
                    ));
        }
        return list;
    }

    public static List<UUID> getFullAccountsList() throws IOException {

        List<UUID> uuidList = new ArrayList<>();

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url + "/list");

        HttpResponse response = client.execute(request);

        JSONObject responseData = new JSONObject(EntityUtils.toString(response.getEntity()));
        JSONArray responseArray = responseData.getJSONArray("list");

        for (int i = 0; i < responseArray.length(); i++) {
            uuidList.add(
                    UUID.fromString(
                            responseArray.getJSONObject(i).getString("UUID")));
        }

        return uuidList;
    }

    public static BigDecimal getAccountAmount(UUID uuid) throws IOException {

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

    public static int transferMoney(UUID from, UUID to, BigDecimal amount) throws IOException {

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
