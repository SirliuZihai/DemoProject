package com.zihai.websocket.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@ChannelHandler.Sharable
@Service
@Scope("prototype")
public class UrlHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final static Logger logger = LoggerFactory.getLogger(UrlHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        logger.info(msg.uri());
        ctx.channel().attr(AttributeKey.newInstance("url")).set(msg.uri());
        ctx.fireChannelRead(msg);
    }
}
