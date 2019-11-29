package com.revolut.assignment.services;

import com.revolut.assignment.datamodel.Account;
import com.revolut.assignment.datamodel.Message;
import com.revolut.assignment.storage.InMemory;
import com.revolut.assignment.utils.Utils;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class Transfer extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        JSONObject sessionObject = Utils.getJSONObject(session);
        UUID from = UUID.fromString(sessionObject.getString("from"));
        UUID to = UUID.fromString(sessionObject.getString("to"));
        BigDecimal delta = new BigDecimal(sessionObject.getString("delta"));

        if (session.getMethod() == NanoHTTPD.Method.POST) {

            Account fromAccount = InMemory.getAccountByUUID(from);
            Account toAccount = InMemory.getAccountByUUID(to);

            if (fromAccount != null && toAccount != null) {

                // TODO transaction
                Message messageFrom = fromAccount.changeValue(delta.negate());
                Message messageTo = null;
                if (messageFrom.getStatus()) {
                    messageTo = toAccount.changeValue(delta);
                }

                JSONObject jFrom = new JSONObject();
                jFrom.put("UUID", from);
                jFrom.put("amount", fromAccount.getValue());
                if (messageFrom != null) {
                    jFrom.put("history", messageFrom.getHistoryIndex());
                    jFrom.put("message", messageFrom.getMessage());
                }

                JSONObject jTo = new JSONObject();
                jTo.put("UUID", to);
                jTo.put("amount", toAccount.getValue());
                if (messageTo != null) {
                    jTo.put("history", messageTo.getHistoryIndex());
                    jTo.put("message", messageTo.getMessage());
                }

                JSONObject result = new JSONObject();
                if (messageFrom != null ) {
                    result.put("status", (messageFrom.getStatus()));
                }
                result.put("delta", delta);
                result.put("from", jFrom);
                result.put("to", jTo);


                return newFixedLengthResponse(NanoHTTPD.Response.Status.ACCEPTED, "application/json",
                        result.toString());

            } else {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                        Utils.ACCOUNT_NOT_EXIST);
            }
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                Utils.WRONG_REQUEST);
    }
}
