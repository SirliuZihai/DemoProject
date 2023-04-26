package client;

import com.zihai.transfer.entity.HeartBeat;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HeartBeatHandler extends SimpleChannelInboundHandler<HeartBeat> {
    final static Logger LOGGER = LoggerFactory.getLogger(HeartBeatHandler.class);
    AtomicInteger atomicInteger = new AtomicInteger(0);
    Bootstrap b;

    public HeartBeatHandler(Bootstrap b) {
        super();
        this.b = b;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeat msg) throws Exception {
        if (msg.getCode() == 0x02) {
            LOGGER.info("get heartBeat return");
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("{} channelRegistered", ctx.channel().id());
        //ctx.channel().attr(AttributeKey.valueOf("bootstrap")).set(b);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("id: {} channelUnregistered", ctx.channel().id());
        // Bootstrap b = (Bootstrap) ctx.channel().attr(AttributeKey.valueOf("bootstrap")).get();
        if (b != null) {
            ctx.channel().eventLoop().schedule(() -> {
                        try {
                            LOGGER.info("{}开始重连", ctx.channel().id());
                            b.connect().sync();
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage());
                        }
                    }
                    , 5, TimeUnit.SECONDS);
        }
        ctx.fireChannelUnregistered();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(new HeartBeat((byte) 0x01)).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        if (future.isSuccess()) {
                            LOGGER.info("{} send heart beat success", ctx.channel().id());
                            atomicInteger.set(0);
                        } else {
                            LOGGER.info("{} send heart beat false", ctx.channel().id());
                            if (atomicInteger.incrementAndGet() > 2) {
                                future.channel().close();
                                atomicInteger.set(0);
                            }
                        }
                    }
                });
            }
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.error("{} 超时", ctx.channel().id());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
