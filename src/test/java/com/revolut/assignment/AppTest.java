package com.revolut.assignment;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.*;

import java.io.StringWriter;
import java.util.UUID;

public class AppTest {
    UUID uuid;

    @BeforeClass
    public static void setUp(){
        App.main(new String [] {});
    }

    @Test
    @Ignore
    public void demoPostRESTAPI() throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();

//        User user = new User();
//        user.setId(100);
//        user.setFirstName("Lokesh");
//        user.setLastName("Gupta");

        StringWriter writer = new StringWriter();
//        JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
//        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//        jaxbMarshaller.marshal(user, writer);

        try {
            //Define a postRequest request
            HttpPost postRequest = new HttpPost("http://localhost:8080/create");

            //Set the API media type in http content-type header
            postRequest.addHeader("content-type", "application/json");

            //Set the request post body
//            StringEntity userEntity = new StringEntity(writer.getBuffer().toString());
//            postRequest.setEntity(userEntity);

            //Send the request; It will immediately return the response in HttpResponse object if any

            HttpResponse response = httpClient.execute(postRequest);

            String responseJSON = EntityUtils.toString(response.getEntity()); // , UTF8_CHARSET

            //verify the valid error code first
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println(response.getEntity().getContentType());
            System.out.println(responseJSON);

            JSONObject jsonObject = new JSONObject(responseJSON.toString());
            uuid = UUID.fromString(jsonObject.getString("UUID"));
            System.out.println("The UUID = " + uuid);

            if (statusCode != 201) {
                System.out.println("fail");
            }
        } finally {
            //Important: Close the connect
            httpClient.getConnectionManager().shutdown();
        }
    }

    @Test
    public void deposit() throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        this.demoPostRESTAPI();
//        User user = new User();
//        user.setId(100);
//        user.setFirstName("Lokesh");
//        user.setLastName("Gupta");

        StringWriter writer = new StringWriter();
//        JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
//        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//        jaxbMarshaller.marshal(user, writer);


        try {
            //Define a postRequest request
            HttpPost postRequest = new HttpPost("http://localhost:8080/deposit");

            //Set the API media type in http content-type header
            postRequest.addHeader("content-type", "application/json");

            //create json object to send
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UUID", uuid.toString());
            jsonObject.put("amount", "1000");
            writer.append(jsonObject.toString());

            //Set the request post body
            StringEntity userEntity = new StringEntity(writer.getBuffer().toString());
            postRequest.setEntity(userEntity);

            //Send the request; It will immediately return the response in HttpResponse object if any

            HttpResponse response = httpClient.execute(postRequest);

            String responseJSON = EntityUtils.toString(response.getEntity()); // , UTF8_CHARSET

            //verify the valid error code first
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println(response.getEntity().getContentType());
            System.out.println(responseJSON);
            if (statusCode != 201) {
                System.out.println("fail");
            }
        } finally {
            //Important: Close the connect
            httpClient.getConnectionManager().shutdown();
        }
    }

}