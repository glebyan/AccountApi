package com.revolut.assignment.controllers;

import com.revolut.assignment.exceptions.AccountNotExistException;
import com.revolut.assignment.exceptions.NotEnoughMoneyException;
import com.revolut.assignment.services.TransferMoneyService;
import com.revolut.assignment.utils.Utils;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class TransferMoneyController extends RouterNanoHTTPD.GeneralHandler {

    private static final Logger logger = Logger.getLogger(TransferMoneyController.class.getName());

    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        JSONObject sessionObject = Utils.getJSONObject(session);

        if (session.getMethod() == NanoHTTPD.Method.POST) {

            TransferMoneyService transferMoneyService = new TransferMoneyService();

            try {
                transferMoneyService.transferMoney(
                        UUID.fromString(sessionObject.getString("from")),
                        UUID.fromString(sessionObject.getString("to")),
                        new BigDecimal(sessionObject.getString("amount"))
                                .setScale(2, RoundingMode.HALF_EVEN));

                return newFixedLengthResponse(NanoHTTPD.Response.Status.ACCEPTED, "application/json",
                        "");
            } catch (SQLException e) {
                logger.warning("SQL Error");
                return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "application/json",
                        "");
            } catch (AccountNotExistException e) {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                        Utils.ACCOUNT_NOT_EXIST);
            } catch (NotEnoughMoneyException e) {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.FORBIDDEN, "application/json",
                        Utils.NOT_ENOUGH_MONEY);
            }

        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                Utils.WRONG_REQUEST);
    }
}
