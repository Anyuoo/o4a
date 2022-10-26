package cn.o4a.rpc.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/22 22:24
 */
public class HeartBeatHandler extends ChannelHandlerDelegate {
    public static final String KEY_READ_TIMESTAMP = "READ_TIMESTAMP";
    public static final String KEY_WRITE_TIMESTAMP = "WRITE_TIMESTAMP";
    private static final Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);

    public HeartBeatHandler(ChannelHandler channelHandler) {
        super(channelHandler);
    }

    @Override
    public void connected(Channel channel) throws RemotingException {
        setReadTimestamp(channel);
        setWriteTimestamp(channel);
        super.connected(channel);
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        clearReadTimestamp(channel);
        clearWriteTimestamp(channel);
        super.disconnected(channel);
    }

    @Override
    public void sent(Channel channel, Message message) throws RemotingException {
        setWriteTimestamp(channel);
        super.sent(channel, message);
    }

    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        setReadTimestamp(channel);
        if (message.isHeartBeatMessage()) {
            //logger.info("receive heart beat: {}", message);
            if (message.isRequest()) {
                final Message heartBeatMessage = Message.heartBeatEvent(message.getId());
                //回执
                sent(channel, heartBeatMessage);

            }
            return;
        }
        super.received(channel, message);
    }

    private void setReadTimestamp(Channel channel) {
        channel.setAttribute(KEY_READ_TIMESTAMP, System.currentTimeMillis());
    }

    private void setWriteTimestamp(Channel channel) {
        channel.setAttribute(KEY_WRITE_TIMESTAMP, System.currentTimeMillis());
    }

    private void clearReadTimestamp(Channel channel) {
        channel.removeAttribute(KEY_READ_TIMESTAMP);
    }

    private void clearWriteTimestamp(Channel channel) {
        channel.removeAttribute(KEY_WRITE_TIMESTAMP);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return this;
    }
}
