package cn.o4a.rpc.common;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/20 15:19
 */
public interface ChannelHandler {

    /**
     * on channel connected.
     *
     * @param channel channel.
     */
    default void connected(Channel channel) throws RemotingException {
    }

    /**
     * on channel disconnected.
     *
     * @param channel channel.
     */
    default void disconnected(Channel channel) throws RemotingException {
    }

    /**
     * on message sent.
     *
     * @param channel channel.
     * @param message message.
     */
    default void sent(Channel channel, Message message) throws RemotingException {
    }

    /**
     * on message received.
     *
     * @param channel channel.
     * @param message message.
     */
    default void received(Channel channel, Message message) throws RemotingException {
    }

    /**
     * on exception caught.
     *
     * @param channel   channel.
     * @param exception exception.
     */
    default void caught(Channel channel, Throwable exception) throws RemotingException {
    }

}
