package com.example.coen6317assignmentak;

import com.example.coen6317assignmentak.controller.AudioController;
import com.example.coen6317assignmentak.model.Audio;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@Import(MockMvcConfig.class)


public class AudioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AudioController controller;

    @Test
    public void testGetAudiorequest() throws Exception {
        String url = "/coen6317/audios/getAudio?artistName=Shakira";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        MvcResult result  = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(200, response.getStatus());
        System.out.println(response.getStatus());
    }

    @Test
    public void testGetAllAudiorequest() throws Exception {
        String url = "/coen6317/audios/getAllAudio";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        MvcResult result  = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(200, response.getStatus());
        System.out.println(response.getStatus());
        System.out.println(response.getContentAsString());
    }

    @Test
    public void testPostAudiorequest() throws Exception {
        String url = "/coen6317/audios/createAudio";
        Audio audio1 = new Audio("Eric Church", "Waka Waka", "Waka Waka", 1, 2010,
                100, 1000);
        Gson gson = new Gson();
        String element = gson.toJson(audio1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(element)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result  = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(200, response.getStatus());
        System.out.println(response.getStatus());
        System.out.println(response.getContentAsString());
    }
}
