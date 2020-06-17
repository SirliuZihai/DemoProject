package com.zihai.websocket.client;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class InternalClient implements CommandLineRunner {
    private static final Logger LOGGER = Logger.getLogger(InternalTask.class);
    static WebsocketHandle clientEndPoint;

    @Override
    public void run(String... args) throws Exception {
        try {
            // open websocket
            clientEndPoint = new WebsocketHandle(new URI("ws://localhost:8987/test"));

        }catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}
