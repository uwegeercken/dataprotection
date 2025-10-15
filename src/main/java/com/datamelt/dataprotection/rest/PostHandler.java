package com.datamelt.dataprotection.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

public class PostHandler implements HttpHandler
{

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        if ("POST".equals(exchange.getRequestMethod()))
        {
            String requestBody = getRequestBody(exchange);
            TransformRequest request = new Gson().fromJson(requestBody, TransformRequest.class);
            String result = calculateResult(request.getInputValue());
            Response response = new Response(UUID.randomUUID().getMostSignificantBits(), result);

            sendResponse(exchange, response);
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private String calculateResult(String inputValue)
    {
        return "test1";
    }

    private String getRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        return requestBody.toString();
    }

    private void sendResponse(HttpExchange exchange, Response response) throws IOException {
        String jsonResponse = new Gson().toJson(response);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonResponse.length());
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }


}
