package cn.o4a.rpc.common;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/22 15:35
 */
public class NettyChannel extends AbsrtactChanel {

    private static final Map<Channel, NettyChannel> CHANNEL_MAP = new ConcurrentHashMap<>();
    private final io.netty.channel.Channel channel;

    private NettyChannel(Ability ability, io.netty.channel.Channel channel, ChannelHandler channelHandler) {
        super(channelHandler, ability);
        if (channel == null) {
            throw new IllegalArgumentException("netty channel == null");
        }
        this.channel = channel;
    }

    public static void removeChannelIfDisconnected(io.netty.channel.Channel channel) {
        if (channel != null && !channel.isOpen()) {
            CHANNEL_MAP.remove(channel);
        }
    }

    public static NettyChannel getOrAddChannel(io.netty.channel.Channel channel, ChannelHandler channelHandler) {
        if (channel == null) {
            return null;
        }
        NettyChannel nettyChannel = CHANNEL_MAP.get(channel);
        if (nettyChannel != null && nettyChannel.isConnected()) {
            return nettyChannel;
        }

        if (channel.isOpen()) {
            nettyChannel = CHANNEL_MAP.put(channel, new NettyChannel(null, channel, channelHandler));
        }

        return nettyChannel;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    @Override
    public boolean isConnected() {
        return channel.isOpen();
    }

    @Override
    public void sent(cn.o4a.rpc.common.Channel channel, Message message, long timeout) throws RemotingException {
        try {
            ChannelFuture future = this.channel.write(message);
            boolean success = future.await(Math.max(0, timeout));

            final Throwable throwable = future.cause();
            if (throwable != null) {
                throw throwable;
            }
            if (!success) {
                throw new RemotingException("Failed to send message " + " to " + getRemoteAddress()
                        + "in timeout(" + timeout + "ms) limit");
            }
        } catch (Throwable e) {
            throw new RemotingException("Failed to send message " + " to " + getRemoteAddress() + ", cause: " + e.getMessage(), e);
        }


    }
}
