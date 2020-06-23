package com.zihai.websocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@ClientEndpoint
@Component
public class WebsocketHandle {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketHandle.class);
    @Autowired
    private TaskBean taskBean;
    private URI endpointURI = new URI("ws://localhost:8987/test");
    private AtomicInteger count =new AtomicInteger(0);
    Session userSession = null;

    public WebsocketHandle() throws URISyntaxException {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        try {
            userSession.close();
        } catch (IOException e) {
            LOGGER.error("onClose",e);
        }
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage(maxMessageSize=1024*500)
    public void onMessage(String message) throws ExecutionException, InterruptedException {
        taskBean.dealMessage(message,count);
    };

    @OnError
    public void onError(Throwable thw){
        try {
            this.userSession.close();
            LOGGER.error("onError",thw);
        } catch (IOException e) {
            LOGGER.error("onError",e);
        }
        restart();
    }

    private void restart(){
        try {
            Thread.sleep(4000);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
            LOGGER.info("reconnect  "+endpointURI.getPath());
        } catch (Exception e) {
            LOGGER.error("restart",e);
        }
    }
    /**
     * Send a message.
     *
     * @param message
     */
    public synchronized void sendMessage(String message) {
        if (this.userSession.isOpen()) {
            LOGGER.info("send:"+message);
            this.userSession.getAsyncRemote().sendText(message);
        } else {
            restart();
            LOGGER.info("send:"+message);
            this.userSession.getAsyncRemote().sendText(message);
        }
    }

}