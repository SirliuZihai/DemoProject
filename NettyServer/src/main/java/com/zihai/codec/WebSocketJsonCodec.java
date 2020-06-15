package com.zihai.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * WebSocket 协议加解密
 */
@Component
@ChannelHandler.Sharable
public class WebSocketJsonCodec extends MessageToMessageCodec<WebSocketFrame, String> {

    private Logger logger = LoggerFactory.getLogger(WebSocketJsonCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, String json, List<Object> list) throws Exception {
        list.add(new TextWebSocketFrame(json));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {
    	ByteBuf byteBuf = webSocketFrame.content();
        String json = convertByteBufToString(byteBuf);
        list.add(json);

        logger.debug("decode: {}", json);
    }
    
    public String convertByteBufToString(ByteBuf buf) {
        String str;
        if(buf.hasArray()) { // 处理堆缓冲区
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
        } else { // 处理直接缓冲区以及复合缓冲区
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, 0, buf.readableBytes());
        }
        return str;
    }

}
