package cn.o4a.rpc.client;

import cn.o4a.rpc.common.Message;
import cn.o4a.rpc.common.NettyChannel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/21 13:39
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private final cn.o4a.rpc.common.ChannelHandler channelHandler;

    public ClientHandler(cn.o4a.rpc.common.ChannelHandler channelHandler) {
        if (channelHandler == null) {
            throw new IllegalArgumentException("channelHandler == null");
        }
        this.channelHandler = channelHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            final NettyChannel nettyChannel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
            channelHandler.connected(nettyChannel);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
        channelHandler.received(channel, (Message) msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        final NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);

        //copy from dubbo
        // We add listeners to make sure our out bound event is correct.
        // If our out bound event has an error (in most cases the encoder fails),
        // we need to have the request return directly instead of blocking the invoke process.
        promise.addListener(future -> {
            if (future.isSuccess()) {
                // if our future is success, mark the future to sent.
                channelHandler.sent(channel, (Message) msg);
                return;
            }

            Throwable t = future.cause();
            if (t != null) {
                final Message message = Message.exceptionEvent(t.toString());
                channelHandler.received(channel, message);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
        try {
            channelHandler.caught(channel, cause);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            final NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
            final Message heartBeatMessage = Message.heartBeatEvent();
            channel.send(heartBeatMessage);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
