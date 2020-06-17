package com.zihai.websocket.client;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import javax.websocket.*;

@ClientEndpoint
public class WebsocketHandle {
    private static final Logger LOGGER = Logger.getLogger(WebsocketHandle.class);
    private URI endpointURI;

    Session userSession = null;

    public WebsocketHandle(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
            this.endpointURI = endpointURI;
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
            LOGGER.error(e);
        }
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message){
        LOGGER.info("receive =="+message);
    };

    @OnError
    public void onError(Throwable thw){
        try {
            this.userSession.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }finally {
            restart();
        }
    }

    private void restart(){
        try {
            Thread.sleep(4000);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
            LOGGER.info("reconnect  "+endpointURI.getPath());
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }
    /**
     * Send a message.
     *
     * @param message
     */
    public synchronized void sendMessage(String message) {
        LOGGER.info("send: 我是hui ");
        if (this.userSession.isOpen()) {
            this.userSession.getAsyncRemote().sendText(message);
        } else {
            restart();
        }
    }

}