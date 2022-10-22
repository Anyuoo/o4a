package cn.o4a.rpc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/22 15:18
 */
public abstract class AbsrtactChanel implements Channel, ChannelHandler {

    protected final ChannelHandler channelHandler;
    protected final Ability ability;
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    protected AbsrtactChanel(ChannelHandler channelHandler, Ability ability) {
        this.channelHandler = channelHandler;
        this.ability = ability;
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

    @Override
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        attributes.remove(key);
    }
}
