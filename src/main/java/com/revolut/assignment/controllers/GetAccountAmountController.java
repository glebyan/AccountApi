package com.revolut.assignment.controllers;

import com.revolut.assignment.AccountNotExistException;
import com.revolut.assignment.App;
import com.revolut.assignment.datamodel.Account;
import com.revolut.assignment.services.DepositAccountService;
import com.revolut.assignment.services.GetAccountAmountService;
import com.revolut.assignment.storage.InMemory;
import com.revolut.assignment.utils.Utils;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class GetAccountAmountController extends RouterNanoHTTPD.GeneralHandler {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        if (session.getMethod() == NanoHTTPD.Method.POST) {

            JSONObject sessionObject = Utils.getJSONObject(session);

            GetAccountAmountService getAccountAmountService = new GetAccountAmountService();

            try {
                BigDecimal amount = getAccountAmountService.getAmount(
                        UUID.fromString(sessionObject.getString("UUID")));

                JSONObject responseJson = new JSONObject();
                responseJson.put("UUID", sessionObject.getString("UUID"));
                responseJson.put("amount", amount.toString());

                return newFixedLengthResponse(NanoHTTPD.Response.Status.ACCEPTED, "application/json",
                        responseJson.toString());
            } catch (SQLException e) {
                logger.warning("SQL Error");
                return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "application/json",
                        "");
            } catch (AccountNotExistException e) {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                        Utils.ACCOUNT_NOT_EXIST);
            }

        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                Utils.WRONG_REQUEST);
    }
}