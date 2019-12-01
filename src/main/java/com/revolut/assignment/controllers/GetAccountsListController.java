package com.revolut.assignment.controllers;

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

public class GetAccountsListController extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        if (session.getMethod() == NanoHTTPD.Method.POST) {

            Collection<UUID> list = InMemory.getAllAccountsUUIDSet();

            JSONArray jsonArray = new JSONArray();

            for (UUID u : list) {
                jsonArray.put(new JSONObject().put("UUID", u.toString()));

            }

            JSONObject object = new JSONObject();
            object.put("list", jsonArray);

            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json",
                    object.toString());

        }

        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                Utils.WRONG_REQUEST);
    }
}