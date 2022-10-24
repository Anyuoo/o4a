package cn.o4a.rpc.common;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 对自定义的channel 封装和缓存
 *
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/22 15:35
 */
public class NettyChannel extends AbstractChanel {

    private static final Map<Channel, NettyChannel> CHANNEL_MAP = new ConcurrentHashMap<>();
    private final io.netty.channel.Channel channel;

    private final AtomicBoolean active = new AtomicBoolean(false);

    private NettyChannel(io.netty.channel.Channel channel, ChannelHandler channelHandler) {
        super(channelHandler);
        if (channel == null) {
            throw new IllegalArgumentException("netty channel == null");
        }
        this.channel = channel;
    }

    public static void removeChannelIfDisconnected(io.netty.channel.Channel channel) {
        if (channel != null && !channel.isActive()) {
            NettyChannel nettyChannel = CHANNEL_MAP.remove(channel);
            if (nettyChannel != null) {
                nettyChannel.markActive(false);
            }
        }
    }

    public static NettyChannel getOrAddChannel(io.netty.channel.Channel channel, ChannelHandler channelHandler) {
        if (channel == null) {
            return null;
        }
        NettyChannel ret = CHANNEL_MAP.get(channel);
        if (ret == null) {
            NettyChannel nettyChannel = new NettyChannel(channel, channelHandler);
            if (channel.isActive()) {
                nettyChannel.markActive(true);
                ret = CHANNEL_MAP.putIfAbsent(channel, nettyChannel);
            }
            if (ret == null) {
                ret = nettyChannel;
            }
        } else {
            ret.markActive(true);
        }
        return ret;
    }

    public static void removeChannel(Channel channel) {
        if (channel != null) {
            NettyChannel nettyChannel = CHANNEL_MAP.remove(channel);
            if (nettyChannel != null) {
                nettyChannel.markActive(false);
            }
        }
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    @Override
    public boolean isConnected() {
        return isActive();
    }

    public boolean isActive() {
        return active.get();
    }

    public void markActive(boolean isActive) {
        active.set(isActive);
    }

    @Override
    public void send(Object message) throws RemotingException {
        send(message, -1);
    }

    @Override
    public void send(Object message, int timeout) throws RemotingException {
        boolean success = true;
        try {
            ChannelFuture future = channel.writeAndFlush(message);
            if (timeout > 0) {
                success = future.await(timeout);
            }

            final Throwable throwable = future.cause();
            if (throwable != null) {
                throw throwable;
            }
            if (!success) {
                throw new RemotingException("Failed to send message " + " to " + getRemoteAddress()
                        + "in timeout(" + timeout + "ms) limit");
            }
        } catch (Throwable e) {
            removeChannelIfDisconnected(channel);
            throw new RemotingException("Failed to send message " + " to " + getRemoteAddress() + ", cause: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        super.close();
        try {
            removeChannelIfDisconnected(channel);
        } catch (Exception e) {
            //
        }

        try {
            channel.close();
        } catch (Exception e) {
            //
        }
    }

}
