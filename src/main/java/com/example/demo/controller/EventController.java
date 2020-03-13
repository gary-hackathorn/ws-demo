package com.example.demo.controller;

import com.example.demo.ReaccomEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    @Value("${websocket_url:ws://localhost:8080/event/}")
    private String webSocketUrl;

    @PostMapping("/event")
    public ReaccomEvent getString(@RequestBody ReaccomEvent reaccomEvent) {



        return reaccomEvent;
    }

}
