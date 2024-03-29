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
    public ResponseEntity<String> geocode(@PathVariable("address") String address) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder uriBuilder = new URIBuilder("https://maps.googleapis.com/maps/api/geocode/json");
            uriBuilder.addParameter("address", address);
            uriBuilder.addParameter("key", apiKey);

            HttpGet httpGet = new HttpGet(uriBuilder.build());

            CloseableHttpResponse response = httpClient.execute(httpGet);

            String jsonResponse = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return ResponseEntity.ok(jsonResponse);
            } else {
                return ResponseEntity.status(response.getStatusLine().getStatusCode()).build();
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
    }
}
