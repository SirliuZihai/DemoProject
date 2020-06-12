import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.zihai.proto.entity.People.Parent;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;
import java.util.Map;

public class ProtoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoTest.class);

    private static String host = "localhost";
    private static int port = 8080;

    public static void main(String[] args) throws InvalidProtocolBufferException {
        Map<String,Object> data = new HashMap<>();
        data.put("name","龙且");
        data.put("age",12);
        data.put("like","独行");
        String obj = new Gson().toJson(data);
        Parent.Builder builder = Parent.newBuilder();
        JsonFormat.parser().merge(obj, builder);
        Parent parent = builder.build();

        byte[] bytes = parent.toByteArray();
        Parent part = Parent.parseFrom(bytes);
        System.out.println(part.getName());

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TestClientHandler(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ByteBuf encoded = ctx.alloc().buffer(bytes.length);
                            encoded.writeBytes(bytes);
                            ctx.write(encoded);
                            ctx.flush();
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
