package com.revolut.assignment.services;

import com.revolut.assignment.datamodel.Account;
import com.revolut.assignment.datamodel.History;
import com.revolut.assignment.datamodel.Message;
import com.revolut.assignment.storage.InMemory;
import com.revolut.assignment.utils.Utils;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class HistoryAccount extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        if (session.getMethod() == NanoHTTPD.Method.POST) {

            JSONObject sessionObject = Utils.getJSONObject(session);
            UUID uuid = UUID.fromString(sessionObject.getString("UUID"));


            if (uuid != null){

                List<History> historyList = InMemory.getAccountByUUID(UUID.fromString(sessionObject.getString("UUID")))
                        .getHistoryList();

                if (historyList != null){
                    JSONArray jsonArray = new JSONArray();

                    for (int i = 0; i < historyList.size(); i ++){
                        jsonArray.put(new JSONObject(historyList.get(i)).put("historyid", i));
                    }

                    JSONObject object = new JSONObject().put("list", jsonArray);

                    return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", object.toString());

                } else {
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                            Utils.ACCOUNT_NOT_EXIST);
                }
            }
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                Utils.WRONG_REQUEST);
    }
}