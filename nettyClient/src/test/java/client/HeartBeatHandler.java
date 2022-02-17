package client;

import com.zihai.transfer.entity.HeartBeat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartBeatHandler extends SimpleChannelInboundHandler<HeartBeat> {
    final static Logger LOGGER = LoggerFactory.getLogger(HeartBeatHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeat msg) throws Exception {
        if(msg.getCode() == 0x02){
            LOGGER.info("get heartBeat return");
        }else{
            ctx.fireChannelRead(msg);
        }
    }

}
