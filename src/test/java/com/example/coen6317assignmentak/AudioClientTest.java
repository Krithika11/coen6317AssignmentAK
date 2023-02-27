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
    Gson gson = new Gson();

    @Test
    public void testGetAudiorequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(getUrl);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(200, response.getStatus());

        System.out.println(response.getStatus());
    }

    @Test
    public void testError404ForGetAudiorequest() throws Exception {
        String ErrorUrl = "/coen6731/audios/geAudio?artistName=Shakira";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(ErrorUrl);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(404, response.getStatus());
        System.out.println(response.getStatus());
    }


    @Test
    public void testGetAllAudioRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(getAllUrl);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(200, response.getStatus());
        System.out.println(response.getStatus());
        System.out.println(response.getContentAsString());
    }

    @Test
    public void testPostAudioRequest() throws Exception {
        // Create POST request object
        Audio audio1 = new Audio("Marie", "Waka Waka", "Waka Waka", 1, 2010,
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
        Executor executor = Executors.newFixedThreadPool(9);

        // Create 3 GET requests
        for (int i = 1; i <= 3; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> executeGetRequest(getAllUrl), executor));
        }
        // Create 3 GET requests
        for (int i = 1; i <= 3; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> executeGetRequest(getUrl), executor));

        }

        // Create 3 POST request object
        for (int i = 1; i <= 3; i++) {
            Audio audio1 = new Audio("Eric Church" + i, "Waka Waka", "Waka Waka", 1, 2010,
                    100, 1000);
            String element1 = gson.toJson(audio1);
            futures.add(CompletableFuture.supplyAsync(() ->
                    executePostRequest(postUrl, element1), executor));
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
    public void testConcurrencyWithFiftyClients() throws ExecutionException, InterruptedException {
        List<CompletableFuture<ApiResponse>> futures = new ArrayList<>();
        Executor executor = Executors.newFixedThreadPool(48);

        // Create 20 GET requests
        for (int i = 1; i <= 20; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                return executeGetRequest(getAllUrl);
            }, executor));
        }
        // Create 20 GET requests
        for (int i = 1; i <= 20; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                return executeGetRequest(getUrl);
            },executor));
        }

        // Create 8 POST requests
        for (int i = 1; i <= 8; i++) {
            // Create POST request object
            Audio audio2 = new Audio("Shakira" +i, "Waka Waka", "Waka Waka", 1, 2010,
                    100, 1000);
            String element2 = gson.toJson(audio2);
            futures.add(CompletableFuture.supplyAsync(() ->
                    executePostRequest(postUrl, element2), executor));
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
        Executor executor = Executors.newFixedThreadPool(99);

        // Create 45 GET requests
        for (int i = 1; i <= 45; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> executeGetRequest(getAllUrl),executor));
        }
        // Create 45 GET requests
        for (int i = 1; i <= 45; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> executeGetRequest(getUrl), executor));
        }

        // Create 9 POST requests
        for (int i = 1; i <= 9; i++) {
            // Create POST request object
            Audio audio2 = new Audio("Ed Sheeran" + i, "Perfect", "Divide", 5, 2015,
                    200, 500 );
            String element2 = gson.toJson(audio2);
            futures.add(CompletableFuture.supplyAsync(() ->
                    executePostRequest(postUrl, element2), executor));
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
