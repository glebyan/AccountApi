package com.revolut.assignment.controllers;

import com.revolut.assignment.services.CreateAccountService;
import com.revolut.assignment.utils.Utils;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONObject;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class CreateAccountController extends RouterNanoHTTPD.GeneralHandler {

    private static final Logger logger = Logger.getLogger(CreateAccountController.class.getName());

    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        if (session.getMethod() == NanoHTTPD.Method.POST) {

            try {
                CreateAccountService service = new CreateAccountService();

                UUID uuid = service.createAccount();

                JSONObject object = new JSONObject().put("UUID", uuid);

                logger.info("account " + uuid + " successfully created");

                return newFixedLengthResponse(NanoHTTPD.Response.Status.CREATED, "application/json",
                        object.toString());
            } catch(SQLException e){

                return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "application/json",
                        "");
            }
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                Utils.WRONG_REQUEST);
    }
}
