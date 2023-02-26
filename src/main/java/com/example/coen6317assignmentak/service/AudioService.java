package com.example.coen6317assignmentak.service;

import com.example.coen6317assignmentak.model.Audio;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AudioService {

    public CompletableFuture<Audio> fetchAudioDetails(String artistName, ConcurrentHashMap<String, Audio> audioMap) {

        Gson gson = new Gson();
        return CompletableFuture.supplyAsync(() -> {
            Audio audio = new Audio();
            if (artistName.equals(null) || artistName.equals("null") || artistName.matches("^\\d+")
                    || artistName.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Artist Name");
            } else {
                    audio = audioMap.get(artistName);
                    System.out.println("GET RESPONSE IN JSON - single element: " + gson.toJson(audio));
                }
            return audio;
            });
    }

    public List<Audio> fetchAllAudioDetails(ConcurrentHashMap<String, Audio> audioMap) {

        List<Audio> audioList = new ArrayList<>();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(audioMap);
            if (audioMap != null) {
                for (Audio a : audioMap.values()) {
                    audioList.add(a);
                }
                System.out.println("GET RESPONSE IN JSON - all elements " + element.toString());
            }
        return audioList;
    }

    public String createAudioDetails(Audio input, ConcurrentHashMap<String, Audio> audioMap) {
        String response = "";
            if (input != null) {
                if (input.getArtistName().matches("^\\d+")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Artist Name");
                } else if (!(audioMap.containsKey(input.getArtistName()))) {
                    audioMap.put(input.getArtistName(), input);
                    response = "Successfully added to the Audio list";
                } else {
                    response = "Audio already exists";
                }
            }
            return response;
        }
}
