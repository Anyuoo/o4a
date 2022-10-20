package cn.o4a.rpc.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/20 15:43
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelDuplexHandler {

    private final Map<String, Channel> channels = new ConcurrentHashMap<>();
    cn.o4a.rpc.common.ChannelHandler channelHandler;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //todo IdleStateEvent
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
