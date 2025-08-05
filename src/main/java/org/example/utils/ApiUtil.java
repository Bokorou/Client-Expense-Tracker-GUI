package org.example.utils;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ApiUtil {
    private static final String SPRINGBOOT_URL = "http://localhost:8080";
    public enum RequestMethod{POST, GET, PUT, DELETE}

    public static HttpURLConnection fetchApi(String apiPath, RequestMethod requestMethod, JsonObject jsonData){
        try{
            //Attempt to create a connection

            URL url = new URL(SPRINGBOOT_URL + apiPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Set the request type we will perform

            conn.setRequestMethod(requestMethod.toString());

            //To send data to API
            if(jsonData != null && requestMethod != RequestMethod.GET){
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                conn.setRequestProperty("Accept", "application/json");

                conn.setDoOutput(true);

                 try(OutputStream os = conn.getOutputStream()){
                    byte[] input = jsonData.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

                return conn;
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;

    }

    //read income data from our API request
    public static String readApiResponse(HttpURLConnection conn){
        try{
            StringBuilder resultJson = new StringBuilder();

            Scanner scanner = new Scanner(conn.getInputStream());

            //Loop through each line in the response and append it t the string builder.

            while(scanner.hasNext()){
                resultJson.append(scanner.nextLine());
            }
            scanner.close();

            return resultJson.toString();

        }catch(IOException e){
            e.printStackTrace();
        }
        return null;

    }

}
