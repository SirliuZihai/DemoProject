package com.zihai.netty.server;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zihai.proto.entity.People;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
@ChannelHandler.Sharable
public class TestServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        System.out.println("channelRead");
        try {
            People.Parent p= (People.Parent)msg;
            People.Son son = People.Son.parseFrom(p.getSon());
            People.Son.Builder s = People.Son.newBuilder();
            s.setAge(son.getAge());
            s.setName(son.getName());
            ctx.writeAndFlush(s.build());
           /* ByteBuf buf = (ByteBuf) msg;
            byte[] array = new byte[buf.readableBytes()];
            buf.readBytes(array);
            People.Parent part = People.Parent.parseFrom(array);
            int length = part.getName().getBytes().length;
            ByteBuf encoded = ctx.alloc().buffer(length);
            encoded.writeBytes(part.getName().getBytes());
            ctx.writeAndFlush(encoded);*/
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("read Complete");
        ctx.fireChannelReadComplete();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有个连接上来了....");
        ctx.fireChannelActive();
    }

}
