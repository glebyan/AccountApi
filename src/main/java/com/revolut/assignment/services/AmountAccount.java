package com.revolut.assignment.services;

import com.revolut.assignment.datamodel.Account;
import com.revolut.assignment.storage.InMemory;
import com.revolut.assignment.utils.Utils;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class AmountAccount extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        JSONObject sessionObject = Utils.getJSONObject(session);
        UUID uuid = UUID.fromString(sessionObject.getString("UUID"));

        if (session.getMethod() == NanoHTTPD.Method.POST) {

            JSONObject object = new JSONObject();

            Account account = InMemory.getAccountByUUID(uuid);

            if (account != null) {
                object.put("UUID", uuid);
                object.put("amount", account.getValue().toString());

                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json",
                        object.toString());

            } else {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                        Utils.ACCOUNT_NOT_EXIST);
            }
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                Utils.WRONG_REQUEST);
    }
}