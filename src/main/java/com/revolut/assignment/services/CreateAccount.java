package com.revolut.assignment.services;

import com.revolut.assignment.storage.InMemory;
import com.revolut.assignment.utils.Utils;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class CreateAccount extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        if (session.getMethod() == NanoHTTPD.Method.POST) {

            UUID uuid = InMemory.crateAccount();

            JSONObject object = new JSONObject().put("UUID", uuid);

            return newFixedLengthResponse(NanoHTTPD.Response.Status.CREATED, "application/json",
                    object.toString());
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                Utils.WRONG_REQUEST);
    }
}
