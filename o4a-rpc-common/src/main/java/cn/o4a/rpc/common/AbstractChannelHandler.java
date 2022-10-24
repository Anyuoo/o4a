package cn.o4a.rpc.common;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/22 22:10
 */
public abstract class AbstractChannelHandler implements ChannelHandler {

    protected final ChannelHandler channelHandler;

    protected AbstractChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    public void connected(Channel channel) throws RemotingException {
        channelHandler.connected(channel);
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        channelHandler.disconnected(channel);
    }

    @Override
    public void sent(Channel channel, Message message) throws RemotingException {
        channelHandler.sent(channel, message);
    }


    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        channelHandler.received(channel, message);
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        channelHandler.caught(channel, exception);
    }

    public ChannelHandler getChannelHandler() {
        if (channelHandler instanceof AbstractChannelHandler) {
            return ((AbstractChannelHandler) channelHandler).getChannelHandler();
        }
        return channelHandler;
    }
}
