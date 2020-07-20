package com.zihai.h2Client.Listener;

import com.zihai.h2Client.util.Constant.LISTENER_EVENT_TYPE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

public class NetWorkEvent extends ApplicationEvent {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetWorkEvent.class);
    private LISTENER_EVENT_TYPE type;

    public NetWorkEvent(LISTENER_EVENT_TYPE type) {
        super(type);
        this.type = type;
    }
    public void handle(){
        LOGGER.info("LISTENER_EVENT_TYPE ==== " + type);
    }

}
