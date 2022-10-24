package cn.o4a.rpc.common;

import java.io.Closeable;
import java.net.InetSocketAddress;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 14:36
 */
public interface Endpoint extends Closeable {
    /**
     * get channel handler.
     *
     * @return channel handler
     */
    ChannelHandler getChannelHandler();

    /**
     * get local address.
     *
     * @return local address.
     */
    InetSocketAddress getLocalAddress();

    /**
     * send message.
     *
     * @param message message
     */
    void send(Object message) throws RemotingException;

    /**
     * send message.
     *
     * @param message message
     * @param timeout already sent to socket?
     */
    void send(Object message, int timeout) throws RemotingException;
}
