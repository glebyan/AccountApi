package com.revolut.assignment.services;

import com.revolut.assignment.datamodel.Account;
import com.revolut.assignment.datamodel.History;
import com.revolut.assignment.storage.InMemory;
import com.revolut.assignment.utils.Utils;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import com.revolut.assignment.datamodel.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class Example extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        if (session.getMethod() == NanoHTTPD.Method.POST) {

            JSONObject sessionObject = Utils.getJSONObject(session);
            String action = sessionObject.getString("action");

            if ("create".equals(action)) {
                UUID uuid = InMemory.crateAccount();
                Message message = new Message(true, "Account UUID:" + uuid +
                        " was sucessfully created", 1L);
                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json",
                        new JSONObject(message).toString());
            }

            if ("getAccountAmountByUUID".equals(action)) {
                Account account = InMemory.getAccountByUUID(UUID.fromString(sessionObject.getString("UUID")));
                JSONObject object = new JSONObject(account);

                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", object.toString());
            }

            if ("getAccountHistoryByUUID".equals(action)){
                // TODO add handling of empty history (NPE)
                List <History> historyList = InMemory.getAccountByUUID(UUID.fromString(sessionObject.getString("UUID")))
                        .getHistoryList();

                JSONArray jsonArray = new JSONArray();

                for (History h : historyList){
                    jsonArray.put(new JSONObject(h));
                }

                JSONObject object = new JSONObject().put("list", jsonArray);

                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", object.toString());
            }

            if ("getAllAccounts".equals(action)) {
                Collection<UUID> list = InMemory.getAllAccountsUUIDSet();
                System.out.println(list);

                JSONArray jsonArray = new JSONArray();

                for (UUID uuid : list){
                    jsonArray.put(new JSONObject().put("UUID", uuid.toString()));
                }

                JSONObject object = new JSONObject();
                object.put("list", jsonArray);

                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json",
                        object.toString());
            }


        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                "The requested resource does not exist");
    }
}
