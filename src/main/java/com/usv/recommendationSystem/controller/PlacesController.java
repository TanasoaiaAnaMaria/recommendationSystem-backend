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
@RequestMapping("/places")
public class PlacesController {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @GetMapping("/search/{keywords}/{latitude}/{longitude}/{radius}")
    public ResponseEntity<String> searchPlace(@PathVariable("keywords") String keywords,
                                               @PathVariable("latitude") double latitude,
                                               @PathVariable("longitude") double longitude,
                                               @PathVariable("radius") int radius) {
        // Make the API call to Google Places API
        // You can use any HTTP client library, e.g., Apache HttpClient, OkHttp, etc.

        // Example API call using Apache HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder uriBuilder = new URIBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
            uriBuilder.addParameter("location", latitude + "," + longitude);
            uriBuilder.addParameter("radius", String.valueOf(radius));
            uriBuilder.addParameter("keyword", keywords);
            uriBuilder.addParameter("key", apiKey);

            HttpGet httpGet = new HttpGet(uriBuilder.build());

            // Execute the request
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // Parse the response
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // Get the response JSON as a String
                String jsonResponse = EntityUtils.toString(response.getEntity());
                System.out.println(jsonResponse);
                return ResponseEntity.ok(jsonResponse);
            } else {
                // Handle error response
                return ResponseEntity.status(response.getStatusLine().getStatusCode()).build();
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search/{id}/{latitude}/{longitude}/{radius}/{params}")
    public ResponseEntity<String> searchPlaces(@PathVariable("id") String id,
                                               @PathVariable("latitude") double latitude,
                                               @PathVariable("longitude") double longitude,
                                               @PathVariable("radius") int radius,
                                               @PathVariable("params") String params) throws URISyntaxException {

        List<String> keywords = Arrays.asList(params.split(" "));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(id + ".txt", false))) {
            for (String keyword : keywords) {
                URIBuilder uriBuilder = new URIBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                        .addParameter("location", latitude + "," + longitude)
                        .addParameter("radius", String.valueOf(radius))
                        .addParameter("keyword", keyword)
                        .addParameter("key", apiKey); // Replace 'apiKey' with your actual API key

                try (CloseableHttpClient httpClient = HttpClients.createDefault();
                     CloseableHttpResponse response = httpClient.execute(new HttpGet(uriBuilder.build()))) {

                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        String jsonResponse = EntityUtils.toString(response.getEntity());
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        JSONArray results = jsonObject.getJSONArray("results");

                        System.out.println(results);

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject place = results.getJSONObject(i);
                            JSONArray types = place.getJSONArray("types");

                            // Check if any of the types of the place match any keyword
                            for (int j = 0; j < types.length(); j++) {
                                if (keywords.contains(types.getString(j))) {
                                    String placeName = place.getString("name");

                                    // Make a second API call to check if there are details available for this place
                                    URIBuilder geocodeUriBuilder = new URIBuilder("https://maps.googleapis.com/maps/api/geocode/json");
                                    geocodeUriBuilder.addParameter("address", placeName);
                                    geocodeUriBuilder.addParameter("key", apiKey);

                                    HttpGet geocodeHttpGet = new HttpGet(geocodeUriBuilder.build());
                                    CloseableHttpResponse geocodeResponse = httpClient.execute(geocodeHttpGet);

                                    if (geocodeResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                        String geocodeJsonResponse = EntityUtils.toString(geocodeResponse.getEntity());

                                        // If there is content in the response, write the place details to the file
                                        if (!geocodeJsonResponse.isEmpty()) {
                                            JSONObject location = place.getJSONObject("geometry").getJSONObject("location");
                                            double placeLatitude = location.getDouble("lat");
                                            double placeLongitude = location.getDouble("lng");

                                            writer.write(placeLatitude + ", " + placeLongitude + ", " + placeName);
                                            writer.newLine();
                                        }
                                    }

                                    break; // Once we've written the place, we don't need to check the rest of its types
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }


}