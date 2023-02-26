package com.example.coen6317assignmentak.controller;

import com.example.coen6317assignmentak.model.Audio;
import com.example.coen6317assignmentak.service.AudioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.out;

@RestController
@RequestMapping("/coen6731/audios")
public class AudioController {

    @Autowired
    AudioService service;

    ConcurrentHashMap<String, Audio> audioDB = new ConcurrentHashMap<>();
    @PostConstruct
    public void init() {
        Audio audio1 = new Audio("Shakira", "Waka Waka", "Waka Waka", 1, 2010,
                100, 1000 );
        Audio audio2 = new Audio("Ed Sheeran", "Perfect", "Divide", 5, 2015,
                200, 500 );

        audioDB.put("Shakira", audio1);
        audioDB.put("Ed Sheeran", audio2);
        int sum = audioDB.values().stream().map(Audio::getCopiesSold).mapToInt(Integer::intValue).sum();

        out.println("Total number of copies sold: " +sum);

    }

    @Async
    @GetMapping(path = "/getAudio", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<Audio>> getAudio(@RequestParam String artistName) {

        return service.fetchAudioDetails(artistName, audioDB)
                .thenApply(getResponse -> ResponseEntity.ok(getResponse));

    }

    @GetMapping(path = "/getAllAudio", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Audio> getAllAudio() {

        List<Audio> getAllAudioResponse = service.fetchAllAudioDetails(audioDB);
        return getAllAudioResponse;

    }

    @PostMapping(path = "/createAudio")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public String createAudio(@RequestBody Audio input) {

        String response = service.createAudioDetails(input,audioDB);

        int sum = audioDB.values().stream().map(Audio::getCopiesSold).mapToInt(Integer::intValue).sum();
        out.println("Total number of copies sold: " +sum);
        return response + " " + input.getArtistName();

    }

}
