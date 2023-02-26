package com.example.coen6317assignmentak;

import com.example.coen6317assignmentak.controller.AudioController;
import com.example.coen6317assignmentak.model.Audio;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@Import(MockMvcConfig.class)


public class AudioClientTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AudioController controller;


    String getUrl = "http://localhost:8080/coen6731/audios/getAudio?artistName=Shakira";
    String getAllUrl = "http://localhost:8080/coen6731/audios/getAllAudio";
    String postUrl = "http://localhost:8080/coen6731/audios/createAudio";

    // Create POST request objects
    Audio audio1 = new Audio("Eric Church", "Waka Waka", "Waka Waka", 1, 2010,
            100, 1000);
    Audio audio2 = new Audio("Marie", "Waka Waka", "Waka Waka", 1, 2010,
            100, 1000);
    Audio audio3 = new Audio("Richie", "Waka Waka", "Waka Waka", 1, 2010,
            100, 1000);
    Gson gson = new Gson();
    String element1 = gson.toJson(audio1);
    String element2 = gson.toJson(audio2);
    String element3 = gson.toJson(audio3);

    @Test
    public void testGetAudiorequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(getUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(200, response.getStatus());

        System.out.println(response.getStatus());
    }

    @Test
    public void testError404ForGetAudiorequest() throws Exception {
        String ErrorUrl = "/coen6731/audios/geAudio?artistName=Shakira";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(ErrorUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(404, response.getStatus());
        System.out.println(response.getStatus());
    }


    @Test
    public void testGetAllAudiorequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(getAllUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(200, response.getStatus());
        System.out.println(response.getStatus());
        System.out.println(response.getContentAsString());
    }

    @Test
    public void testPostAudioRequest() throws Exception {
        Audio audio1 = new Audio("Eric Church", "Waka Waka", "Waka Waka", 1, 2010,
                100, 1000);
        Gson gson = new Gson();
        String element = gson.toJson(audio1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(postUrl)
                .accept(MediaType.APPLICATION_JSON)
                .content(element)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(201, response.getStatus());
        System.out.println(response.getStatus());
        System.out.println(response.getContentAsString());
    }

    @Test
    public void testConcurrencyWithNineClients() throws ExecutionException, InterruptedException {
        List<CompletableFuture<ApiResponse>> futures = new ArrayList<>();

        // Create 20 GET requests
        for (int i = 1; i <= 3; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                return executeGetRequest(getAllUrl);
            }));
        }
        // Create 20 GET requests
        for (int i = 1; i <= 3; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> executeGetRequest(getUrl)));
        }

            futures.add(CompletableFuture.supplyAsync(() ->
                    executePostRequest(postUrl, element1)));
            futures.add(CompletableFuture.supplyAsync(() ->
                    executePostRequest(postUrl, element2)));
            futures.add(CompletableFuture.supplyAsync(() ->
                    executePostRequest(postUrl, element3)));

        // Wait for all requests to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        allFutures.join();

        // Iterate over the results
        for (CompletableFuture<ApiResponse> future : futures) {
            ApiResponse apiResponse = future.get();
            String response = apiResponse.getResponse();
            long duration = apiResponse.getDuration();
            System.out.println("Response: " + response);
            System.out.println("Duration: " + duration + " milliseconds");
        }

    }

    @Test
    public void testConcurrencyWithFiftyClients() throws ExecutionException, InterruptedException {
        List<CompletableFuture<ApiResponse>> futures = new ArrayList<>();

        // Create 20 GET requests
        for (int i = 1; i <= 20; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                return executeGetRequest(getAllUrl);
            }));
        }
        // Create 20 GET requests
        for (int i = 1; i <= 20; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> executeGetRequest(getUrl)));
        }

        // Create 8 POST requests
        for (int i = 1; i <= 8; i++) {
            futures.add(CompletableFuture.supplyAsync(() ->
                    executePostRequest(postUrl, element3)));
        }

        // Wait for all requests to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        allFutures.join();

        // Iterate over the results
        for (CompletableFuture<ApiResponse> future : futures) {
            ApiResponse apiResponse = future.get();
            String response = apiResponse.getResponse();
            long duration = apiResponse.getDuration();
            System.out.println("Response: " + response);
            System.out.println("Duration: " + duration + " milliseconds");
        }

    }

    @Test
    public void testConcurrencyWithHundredClients() throws ExecutionException, InterruptedException {
        List<CompletableFuture<ApiResponse>> futures = new ArrayList<>();

        // Create 45 GET requests
        for (int i = 1; i <= 45; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                return executeGetRequest(getAllUrl);
            }));
        }
        // Create 45 GET requests
        for (int i = 1; i <= 45; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> executeGetRequest(getUrl)));
        }

        // Create 9 POST requests
        for (int i = 1; i <= 9; i++) {

            futures.add(CompletableFuture.supplyAsync(() ->
                    executePostRequest(postUrl, element2)));
        }

        // Wait for all requests to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        allFutures.join();

        // Iterate over the results
        for (CompletableFuture<ApiResponse> future : futures) {
            ApiResponse apiResponse = future.get();
            String response = apiResponse.getResponse();
            long duration = apiResponse.getDuration();
            System.out.println("Response: " + response);
            System.out.println("Duration: " + duration + " milliseconds");
        }

    }

    public static ApiResponse executeGetRequest(String url) {
        long startTime = System.currentTimeMillis();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        long duration = System.currentTimeMillis() - startTime;
        return new ApiResponse(responseEntity.getBody(), duration);
    }

    public static ApiResponse executePostRequest(String url, String requestBody) {
        long startTime = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        long duration = System.currentTimeMillis() - startTime;
        return new ApiResponse(responseEntity.getBody(), duration);
    }

    public static class ApiResponse {
        private String response;
        private long duration;

        public ApiResponse(String response, long duration) {
            this.response = response;
            this.duration = duration;
        }
        public String getResponse() {
            return response;
        }
        public long getDuration() {
            return duration;
        }
    }
}
