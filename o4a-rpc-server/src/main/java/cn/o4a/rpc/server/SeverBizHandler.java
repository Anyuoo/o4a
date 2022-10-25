package cn.o4a.rpc.server;

import cn.o4a.rpc.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 20:37
 */
public class SeverBizHandler implements ChannelHandler {
    private static final Logger logger = LoggerFactory.getLogger(SeverBizHandler.class);


    @Override
    public void disconnected(Channel channel) throws RemotingException {
        logger.info("============> Sever Handler: channel disconnected");
    }

    @Override
    public void connected(Channel channel) throws RemotingException {
        logger.info("============> Sever Handler: channel connected");
    }

    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        final MessageCode code = message.getCode();
        logger.info("============> Sever Handler: channel received,  code:{}", code);
        if (code == MessageCode.TASK_COMPLETED) {
            //

        } else {
            switch (code) {
                case NORMAL:

                    break;
                case REGISTER:
                    break;
                default:
                    throw new IllegalStateException("不可达状态");
            }
        }
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        logger.info("============> Sever Handler: channel received");
    }
}
