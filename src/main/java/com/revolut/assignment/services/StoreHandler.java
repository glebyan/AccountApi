package com.revolut.assignment.services;

import com.revolut.assignment.datamodel.Message;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class StoreHandler extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(
            RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        if (session.getMethod() == NanoHTTPD.Method.POST) {
            try {
                final HashMap<String, String> map = new HashMap<String, String>(); // создаем объект, куда мапить содержимое реквеста
                session.parseBody(map); // получаем коллекцию содержимого реквеста
                final String json = map.get("postData"); // берем Body
                System.out.println(json);
                String requestBody = session.getQueryParameterString();
//                return newFixedLengthResponse("Request body = " + requestBody);

                // парсим json
                JSONObject object = new JSONObject(json);
                String from = object.getString("from");
                String to = object.getString("to");
                Integer amount = object.getInt("amount");

                // создаем json
                Message message = new Message(true, from + to + amount.toString(), 123L);

                JSONObject jsonObject = new JSONObject(message);
                String myJson = jsonObject.toString();


                // возвращаем результат
                return newFixedLengthResponse(myJson);




            } catch (IOException | NanoHTTPD.ResponseException e) {
                // handle
                e.printStackTrace();
            }
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                "The requested resource does not exist");
    }
}
