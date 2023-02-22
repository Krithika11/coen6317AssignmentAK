package com.example.coen6317assignmentak.service;

import com.example.coen6317assignmentak.model.Audio;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AudioService {

    public Audio fetchAudioDetails(String artistName, ConcurrentHashMap<String, Audio> audioMap) {
        Audio audio = new Audio();

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(audioMap);
        if (artistName != null) {
            audio = audioMap.get(artistName);
        }
        System.out.println("GET RESPONSE IN JSON - single element: " + gson.toJson(audio));
        return audio;

    }

    public List<Audio> fetchAllAudioDetails(ConcurrentHashMap<String, Audio> audioMap) {

        List<Audio> audioList = new ArrayList<>();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(audioMap);

        for(Audio a:audioMap.values()){
            audioList.add(a);
        }
        System.out.println("GET RESPONSE IN JSON - all elements " + element.toString());

        return audioList;
    }

    public String createAudioDetails(Audio input, ConcurrentHashMap<String, Audio> audioMap) {

        audioMap.put(input.getArtistName(), input);
        String response = "Successfully added to the Audio list";
        return response;
    }
}
