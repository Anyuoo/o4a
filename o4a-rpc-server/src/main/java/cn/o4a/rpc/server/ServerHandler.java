package cn.o4a.rpc.server;

import cn.o4a.rpc.common.Message;
import cn.o4a.rpc.common.NettyChannel;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/20 15:43
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private final Map<String, Channel> channels = new ConcurrentHashMap<>();
    private final cn.o4a.rpc.common.ChannelHandler channelHandler;

    public ServerHandler(cn.o4a.rpc.common.ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    public Map<String, Channel> getChannels() {
        return channels;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
        channel.connected(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
        try {
            channelHandler.disconnected(channel);
        } finally {
            NettyChannel.removeChannel(ctx.channel());
        }

        if (logger.isInfoEnabled()) {
            logger.info("The connection of " + channel.getRemoteAddress() + " -> " + channel.getLocalAddress() + " is disconnected.");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
        final Message message = (Message) msg;
        channelHandler.received(channel, message);
    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //移交下一个节点
        super.write(ctx, msg, promise);
        //操作
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
        final Message message = (Message) msg;
        channelHandler.sent(channel, message);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // server will close channel when server don't receive any heartbeat from client util timeout.
        if (evt instanceof IdleStateEvent) {
            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
            try {
                logger.info("IdleStateEvent triggered, close channel " + channel);
                channel.close();
            } finally {
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
        try {
            channelHandler.caught(channel, cause);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }
}
