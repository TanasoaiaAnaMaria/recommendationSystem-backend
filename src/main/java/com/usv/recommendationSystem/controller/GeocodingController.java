package com.usv.recommendationSystem.controller;

import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/geocoding")
public class GeocodingController {

    @Value("${google.maps.api.key}")
    private String apiKey;
    @GetMapping("/geocode/{address}")
    public  ResponseEntity<Integer> geocode(@PathVariable("address") String address) {
        // Make the API call to Google Maps Geocoding API
        // You can use any HTTP client library, e.g., Apache HttpClient, OkHttp, etc.

        // Example API call using Apache HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder uriBuilder = new URIBuilder("https://maps.googleapis.com/maps/api/geocode/json");
            uriBuilder.addParameter("address", address);
            uriBuilder.addParameter("key", apiKey);

            HttpGet httpGet = new HttpGet(uriBuilder.build());

            // Execute the request
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // Parse the response
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // Get the response JSON as a String
                String jsonResponse = EntityUtils.toString(response.getEntity());
                System.out.println(jsonResponse);
                // Count the number of characters in the JSON response
                int characterCount = jsonResponse.length();

                return ResponseEntity.ok(characterCount);
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
