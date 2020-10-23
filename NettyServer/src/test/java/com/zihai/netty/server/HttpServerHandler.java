package com.zihai.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

@ChannelHandler.Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final static Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if(!req.decoderResult().isSuccess()){
            ctx.close();
        }
        logger.info("url:{}",req.uri());
        HttpHeaders httpHeaders = req.headers();
        Iterator<Map.Entry<String, String>> iterator = httpHeaders.iteratorAsString();
        while (iterator.hasNext()){
            Map.Entry entry = iterator.next();
            logger.info(entry.getKey()+" "+entry.getValue());
        }
        logger.info("content {}",convertByteBufToString(req.content()));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
        response.headers().add("Content-Type","application-json");
        response.content().writeBytes("hello, 我是服务器".getBytes());
        //ByteBuf buf2 = Unpooled.copiedBuffer(new StringBuffer("好男人"),Charset.defaultCharset());
        //buf2.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
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
