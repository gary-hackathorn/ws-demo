package com.example.demo.controller;

import com.example.demo.ReaccomEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebAppConfiguration
public class EventControllerTest {

    @Value("${local.server.port}")
    private int port;
    private String URL;
    private static final String EVENT_ENDPOINT = "/topic/event/";
    private CompletableFuture<ReaccomEvent> completableFuture;

//    @Autowired
//    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//        port = 8080;
        URL = "ws://localhost:" + port + "/event";
        completableFuture = new CompletableFuture<>();
    }

    @Test
    public void testGetEndpoint() throws Exception {
//        MvcResult mvcResult = this.mockMvc.perform(get("/event")).andReturn();
//        assertEquals(200, mvcResult.getResponse().getStatus());
//        assertEquals("hello gary", mvcResult.getResponse().getContentAsString());

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        ReaccomEvent event = new ReaccomEvent();
        event.setType("FLIGHT_EVENT");
        event.setKey("FLIGHT");
        event.setValue("flt1");

        stompSession.subscribe(EVENT_ENDPOINT, new CreateStompFrameHandler());
        stompSession.send(EVENT_ENDPOINT, event);
        ReaccomEvent reaccomEvent = completableFuture.get(10, SECONDS);
        System.out.println(reaccomEvent);
        assertNotNull(reaccomEvent);
        System.out.println("Message sent");
    }
    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }
    private class CreateStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            System.out.println(stompHeaders.toString());
            return ReaccomEvent.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println(o);
            completableFuture.complete((ReaccomEvent) o);
        }
    }
}
