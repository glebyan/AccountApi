package com.revolut.assignment.utils;

import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Utils {
    private Utils(){}

    public static final String WRONG_REQUEST = "{\"errormessage\":\"wrong request type\"}";
    public static final String ACCOUNT_NOT_EXIST = "{\"errormessage\":\"given account not exist\"}";

    public static JSONObject getJSONObject(NanoHTTPD.IHTTPSession session){
        final HashMap<String, String> map = new HashMap<String, String>(); // создаем объект, куда мапить содержимое реквеста
        try {
            session.parseBody(map); // получаем коллекцию содержимого реквеста
        } catch (IOException | NanoHTTPD.ResponseException e) {
            e.printStackTrace();
        }
        final String json = map.get("postData"); // берем Body
        System.out.println(json);

        return new JSONObject(json);
    }
}
