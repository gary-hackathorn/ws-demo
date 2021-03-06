package com.example.demo.controller;

import com.example.demo.ReaccomEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
public class EventController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/event")
    public ReaccomEvent getString(@RequestBody ReaccomEvent reaccomEvent) throws InterruptedException, ExecutionException, TimeoutException {
        this.sendEvent(reaccomEvent);
        return reaccomEvent;
    }


    public void sendEvent(ReaccomEvent event){
        simpMessagingTemplate.convertAndSend("/topic/event/", event);
    }

}
