package com.zihai.websocket.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 ** 消息处理类
 */
@Sharable
@Service
@Scope("prototype")
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(ChatServerHandler.class);
    private static ConcurrentHashMap<String,Integer> channelMap = new ConcurrentHashMap<>();

    @Value("${server.id}")
    private int serverId;

    /**
     **   客户端连接
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端与服务端连接开启:  {}", ctx.channel().id().asLongText());
        channelMap.put(ctx.channel().id().asLongText(),0);
        //map.put(ctx.channel().id().asLongText(),ctx);

    }

    /**
     **   客户端关闭
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
        logger.info("断开连接：{}",ctx.channel().id().asLongText());
        channelMap.remove(ctx.channel().id().asLongText());
    }

    /**
     **  读取消息
     *
     * @param ctx   通道上下文
     * proto 协议
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String json) {
        Integer count = channelMap.get(ctx.channel().id().asLongText());
        logger.info("收到消息：{},count:{}", json,count++);
        channelMap.put(ctx.channel().id().asLongText(),count);
        ctx.channel().writeAndFlush(json+"收到count:"+count);
    }

    /**
     **  异常消息
     *
     * @param ctx   通道上下文
     * @param cause 线程
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("异常消息", cause);
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
            if(event.state() == IdleState.READER_IDLE){
                logger.info("websocket 超时事件触发-------->接收消息超时");
                ctx.channel().close();
                logger.info("websocket 超时事件触发-------->关闭不活动的链接：" + ctx.channel().id().asLongText());
            }else{
                super.userEventTriggered(ctx,evt);
            }
        }
    }
}
