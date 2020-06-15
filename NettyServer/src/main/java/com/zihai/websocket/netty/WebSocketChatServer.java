package com.zihai.websocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * websocket server
 */
@Component("webSocketChatServer")
public class WebSocketChatServer {

    private Logger logger = LoggerFactory.getLogger(WebSocketChatServer.class);

    @Value("${server.websocket.port:9090}")
    private int port;
    @Autowired
    private WebSocketServerInitializer serverInitializer;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(5);
    private final EventLoopGroup workGroup = new NioEventLoopGroup(10);

    private ChannelFuture channelFuture;

    public void start() throws Exception {
        try {
            ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverInitializer);

            logger.info("Starting WebSocketChatServer... Port: " + port);

            channelFuture = b.bind(port).sync();
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    shutdown();
                }
            });
        }
    }

    public void restart() throws Exception {
        shutdown();
        start();
    }

    public void shutdown() {
        if (channelFuture != null) {
            channelFuture.channel().close().syncUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }

}
