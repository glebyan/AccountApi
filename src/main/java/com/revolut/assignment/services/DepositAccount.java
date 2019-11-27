package com.revolut.assignment.services;

/* DEPOSITE CACHE IN ATM
 * is change state? YES!
 *
 */

import com.revolut.assignment.datamodel.Account;
import com.revolut.assignment.datamodel.Message;
import com.revolut.assignment.storage.InMemory;
import com.revolut.assignment.utils.Utils;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class DepositAccount extends RouterNanoHTTPD.GeneralHandler {


    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        if (session.getMethod() == NanoHTTPD.Method.POST) {

            JSONObject sessionObject = Utils.getJSONObject(session);

            JSONObject object = new JSONObject();

            Account account = InMemory.getAccountByUUID(UUID.fromString(sessionObject.getString("UUID")));

            BigDecimal delta = new BigDecimal(sessionObject.getString("amount")).setScale(2, RoundingMode.HALF_EVEN);

            if (account != null) {

                //TODO add null check or nullpointerexception catching

                object.put("UUID", sessionObject.getString("UUID"));
                object.put("before", account.getValue());
                object.put("delta", delta);
                Message message = account.changeValue(delta);
                object.put("current", account.getValue());
                object.put("message", message.getMessage());
                object.put("history", message.getHistoryIndex());

                if (message.getStatus()){
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.ACCEPTED, "application/json",
                            object.toString());
                } else {
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.FORBIDDEN, "application/json",
                            object.toString());
                }

            } else {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                        Utils.ACCOUNT_NOT_EXIST);
            }
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                Utils.WRONG_REQUEST);
    }
}

