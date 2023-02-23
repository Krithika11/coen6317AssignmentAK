package com.example.coen6317assignmentak.controller;

import com.example.coen6317assignmentak.model.Audio;
import com.example.coen6317assignmentak.service.AudioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.out;

@RestController
@RequestMapping("/coen6317/audios")
@Api(value =  "Audio API", protocols = "http")
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

    @ApiOperation(value = "To access a particular Audio details by passing artist name", response = Audio.class, code =200)
    @GetMapping(path = "/getAudio", produces = MediaType.APPLICATION_JSON_VALUE)
    @Async
    public Audio getAudio(@RequestParam String artistName) {

        Audio getAudioResponse = service.fetchAudioDetails(artistName, audioDB);
        return getAudioResponse;

    }

    @ApiOperation(value = "To access a particular Audio details by passing artist name", response = Audio.class, code =200)
    @GetMapping(path = "/getAllAudio", produces = MediaType.APPLICATION_JSON_VALUE)
    @Async
    public List<Audio> getAllAudio() {

        List<Audio> getAllAudioResponse = service.fetchAllAudioDetails(audioDB);
        return getAllAudioResponse;

    }

    @ApiOperation(value = "To access a particular Audio details by passing artist name", response = Audio.class, code =200)
    @PostMapping(path = "/createAudio")
    @ResponseBody
    @Async
    public String createAudio(@RequestBody Audio input) {

        String response = service.createAudioDetails(input,audioDB);
        return response + " " + input.getArtistName();

    }

}
