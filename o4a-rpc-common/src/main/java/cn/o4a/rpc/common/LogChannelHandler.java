package cn.o4a.rpc.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 15:29
 */
public class LogChannelHandler implements ChannelHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogChannelHandler.class);

    @Override
    public void connected(Channel channel) throws RemotingException {
        logger.info("connected");
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        logger.info("disconnected");
    }

    @Override
    public void sent(Channel channel, Message message) throws RemotingException {
        logger.info("sent: {}", message);
    }

    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        logger.info("received");
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        logger.info("caught");
    }
}
