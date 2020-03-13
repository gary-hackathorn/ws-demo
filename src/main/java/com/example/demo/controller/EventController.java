package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    @Value("${websocket_url:ws://localhost:8080/event/}")
    private String webSocketUrl;

    @GetMapping("/event")
    public String getString() {
        return "hello gary";
    }

}
