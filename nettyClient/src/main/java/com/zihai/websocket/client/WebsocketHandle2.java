package com.zihai.websocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@ClientEndpoint
@Component
public class WebsocketHandle2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketHandle2.class);
    @Autowired
    private TaskBean taskBean;
    private URI endpointURI = new URI("ws://localhost:8987/test");
    private AtomicInteger count =new AtomicInteger(0);
    Session userSession = null;

    public WebsocketHandle2() throws URISyntaxException {
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
        LOGGER.info("opening websocket");
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
        LOGGER.info("closing websocket with resason code "+reason.getCloseCode());
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
    @OnMessage(maxMessageSize=100)
    public void onMessage(String message) throws ExecutionException, InterruptedException {
        try {
            LOGGER.info(taskBean.dealMessage(message,count));
        }catch (RuntimeException e){
            LOGGER.info(e.getMessage());
        }


    }

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
    public synchronized void sendMessage(ByteBuffer buf) throws IOException {
        if (this.userSession.isOpen()) {
            this.userSession.getBasicRemote().sendBinary(buf);
        } else {
            restart();
            this.userSession.getBasicRemote().sendBinary(buf);

        }
    }

}