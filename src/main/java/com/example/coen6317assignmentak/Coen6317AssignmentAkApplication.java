package com.example.coen6317assignmentak;

import com.example.coen6317assignmentak.controller.AudioController;
import com.example.coen6317assignmentak.model.Audio;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.example.coen6317assignmentak.controller", "com.example.coen6317assignmentak.service"})
public class Coen6317AssignmentAkApplication {

    @Autowired
    private AudioController audioController;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        SpringApplication.run(Coen6317AssignmentAkApplication.class, args);
        }

}
