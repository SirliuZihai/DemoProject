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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ProtoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoTest.class);

    private static String host = "localhost";
    private static int port = 8080;
    private static ChannelHandlerContext ctx2 = null;
    public static void main(String[] args) throws InvalidProtocolBufferException {
        People.Son son = People.Son.newBuilder().setAge(23).setName("小兵").build();
        People.Parent parent = People.Parent.newBuilder()
                .setAge(11).setName("龙且").setLike("喜好").setSon(son.toByteString()).build();
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    Assert.notNull(ctx2,"ctx2 is null");
                    ctx2.writeAndFlush(parent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    ch.pipeline().addLast(new ProtobufDecoder(People.Son.getDefaultInstance()));
                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                    ch.pipeline().addLast(new ProtobufEncoder());
                    ch.pipeline().addLast(new TestClientHandler(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            LOGGER.info("socket is activity");
                            ctx2 = ctx;
                            //ctx.writeAndFlush(part);
                            /*ByteBuf encoded = ctx.alloc().buffer(bytes.length);
                            encoded.writeBytes(bytes);
                            ctx.writeAndFlush(encoded);*/
                        }
                    });
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
