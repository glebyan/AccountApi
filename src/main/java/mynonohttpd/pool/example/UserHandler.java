package mynonohttpd.pool.example;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;

public class UserHandler extends RouterNanoHTTPD.DefaultHandler {
    @Override
    public String getText() {
        return "UserA, UserB, UserC";
    }
 
    @Override
    public String getMimeType() {
        return MIME_PLAINTEXT;
    }
 
    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.OK;
    }
}