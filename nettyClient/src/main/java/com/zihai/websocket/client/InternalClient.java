package com.zihai.websocket.client;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.websocket.ClientEndpoint;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class InternalClient implements CommandLineRunner {
    static WebsocketHandler clientEndPoint;

    @Override
    public void run(String... args) throws Exception {
        try {
            // open websocket
            clientEndPoint = new WebsocketHandler(new URI("wss://localhost:8987/test"));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketHandler.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

        }catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}
