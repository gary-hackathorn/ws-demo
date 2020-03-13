package com.example.demo.controller;

import com.example.demo.ReaccomEvent;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @SendTo("/topic/event/")
    public ReaccomEvent sendEvent(ReaccomEvent event){
        return event;
    }
}
