package client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zihai.proto.entity.People;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ProtoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoTest.class);

    private static String host = "localhost";
    private static int port = 8080;
    public static void main(String[] args) throws InvalidProtocolBufferException {
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.remoteAddress(host, port);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new IdleStateHandler(20,8,0));
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    ch.pipeline().addLast(new ProtobufDecoder(People.Son.getDefaultInstance()));
                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                    ch.pipeline().addLast(new ProtobufEncoder());
                    //ch.pipeline().addLast(new HeartBeatHandler());
                    ch.pipeline().addLast(new TestClientHandler(){
                        @Override
                        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                            LOGGER.info("{} channelRegistered",ctx.channel().id());
                            ctx.channel().attr(AttributeKey.valueOf("bootstrap")).set(b);
                        }
                    });
                }
            });

            // Start the client.
            ChannelFuture f = b.connect().sync(); // (5)
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
            LOGGER.info("go closeFutre");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        } catch (Error e){
            LOGGER.error(e.getMessage(),e);
        }finally {
            /*LOGGER.info("workerGroup.shutdownGracefully");
            workerGroup.shutdownGracefully();*/
        }
        while (true){

        }
    }
}
