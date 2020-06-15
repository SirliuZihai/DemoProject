package com.zihai.websocket.netty;

import com.zihai.codec.WebSocketJsonCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * WebSocket服务初始化类
 */
@Component
public class WebSocketServerInitializer extends ChannelInitializer<NioSocketChannel> {

    @Autowired
    private WebSocketJsonCodec jsonCodec;
    @Autowired
    private ChatServerHandler serverHandler;

    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 编解码 http 请求
        pipeline.addLast(new HttpServerCodec());
        // 写文件内容
        pipeline.addLast(new ChunkedWriteHandler());
        // 聚合解码 HttpRequest/HttpContent/LastHttpContent 到 FullHttpRequest
        // 保证接收的 Http 请求的完整性
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        // 处理其他的 WebSocketFrame
        pipeline.addLast(new WebSocketServerProtocolHandler("/test", null, false, 1024 * 1024 * 2));
        //超时事件
        pipeline.addLast(new IdleStateHandler(40, 0, 0, TimeUnit.SECONDS));
        // 处理 TextWebSocketFrame
        pipeline.addLast(jsonCodec);
        pipeline.addLast(serverHandler);
    }

}
