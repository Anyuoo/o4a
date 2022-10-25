package cn.o4a.rpc.client;

import cn.o4a.rpc.common.*;

import java.net.InetSocketAddress;

/**
 * 重连处理器
 *
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/25 14:59
 */
public class ReconnectHandler extends ChannelHandlerDelegate {
    private final ClientCluster clientCluster;

    protected ReconnectHandler(ChannelHandler channelHandler, ClientCluster clientCluster) {
        super(channelHandler);
        this.clientCluster = clientCluster;
    }

    @Override
    public void connected(Channel channel) throws RemotingException {
        //保存之前的服务地址
        channel.setAttribute(Constants.CHANNEL_KEY_SEVER_ADDRESS, channel.getRemoteAddress());
        super.connected(channel);
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        super.disconnected(channel);
        //触发重连
        final InetSocketAddress severAddress = (InetSocketAddress) channel.getAttribute(Constants.CHANNEL_KEY_SEVER_ADDRESS);
        clientCluster.getOrRegister(severAddress);
    }
}
