package client;

import com.zihai.proto.entity.People;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import utils.ProtoUtil;

@ChannelHandler.Sharable
public class TestClientHandler extends ChannelInboundHandlerAdapter {
    final static Logger LOGGER = LoggerFactory.getLogger(TestClientHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            People.Son s = (People.Son) msg; // (1)
            LOGGER.info(s.getName());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error(cause.getMessage(),cause);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //LOGGER.info("{} read Complete" ,ctx.channel().id());
        ctx.fireChannelReadComplete();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //LOGGER.info("{} channelActive...." ,ctx.channel().id());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //LOGGER.info("{} channelInactive",ctx.channel().id());
        ctx.fireChannelInactive();
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //LOGGER.info("{} channelRegistered",ctx.channel().id());
        ctx.fireChannelRegistered();
    }
    /**
     **  客户端超时事件处理
     *
     * @param ctx   通道上下文
     * @param evt 事件
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if(event.state() == IdleState.WRITER_IDLE) {
                People.Son son = People.Son.newBuilder().setAge(23).setName("小兵").build();
                People.Parent parent = People.Parent.newBuilder()
                        .setAge(11).setName("龙且").setLike("喜好").setSon(son).build();
                LOGGER.info(ProtoUtil.proto2json(parent));
                //JsonFormat.parser().merge("json", People.Parent.newBuilder());
                Assert.notNull(ctx, "ctx is null");
                ctx.writeAndFlush(parent).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        if (future.isSuccess()) {
                            LOGGER.info("{} send heart beat success", ctx.channel().id());
                        } else {
                            LOGGER.info("{} send heart beat false", ctx.channel().id());
                        }
                    }
                });
            }
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
