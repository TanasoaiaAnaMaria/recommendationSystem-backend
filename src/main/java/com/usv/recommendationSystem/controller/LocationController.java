package com.usv.recommendationSystem.controller;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @GetMapping("/name/{latitude}/{longitude}")
    public ResponseEntity<String> getLocationName(@PathVariable("latitude") double latitude, @PathVariable("longitude") double longitude) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder uriBuilder = new URIBuilder("https://maps.googleapis.com/maps/api/geocode/json");
            uriBuilder.addParameter("latlng", latitude + "," + longitude);
            uriBuilder.addParameter("key", apiKey);

            HttpGet httpGet = new HttpGet(uriBuilder.build());

            // Execute the request
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // Parse the response
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // Get the response JSON as a String
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray results = jsonObject.getJSONArray("results");

                if(results.length() > 0) {
                    JSONObject firstResult = results.getJSONObject(0);
                    String formattedAddress = firstResult.getString("formatted_address");
                    return ResponseEntity.ok(formattedAddress);
                } else {
                    return ResponseEntity.status(HttpStatus.SC_NO_CONTENT).build();
                }
            } else {
                // Handle error response
                return ResponseEntity.status(response.getStatusLine().getStatusCode()).build();
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
    }
}
