package cn.o4a.rpc.client;

import cn.newrank.niop.sdk.consumer.AbilityTaskHandler;
import cn.newrank.niop.sdk.model.ConsumerMessage;
import cn.o4a.rpc.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 11:24
 */
public class BizHandler implements ChannelHandler {

    private static final Logger logger = LoggerFactory.getLogger(BizHandler.class);

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        logger.info("============> Client Handler: channel disconnected");
    }

    @Override
    public void connected(Channel channel) throws RemotingException {
        logger.info("============> Client Handler: channel connected");
    }

    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        final MessageCode code = message.getCode();
        logger.info("============> Client Handler: channel received, code:{}", code);
        if (code == MessageCode.TASK_DISPATCH) {
            //
            final AbilityTaskHandler abilityTaskHandler = TaskExecuteHandlers.get("ability_id");
            final ConsumerMessage consumerMessage = abilityTaskHandler.handle(null);
            channel.send(Message.response(message.getId(), Message.STATUS_OK, consumerMessage));
        } else {
            switch (code) {
                case NORMAL:
                    break;
                default:
                    throw new IllegalStateException("不可达状态");
            }
        }
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        logger.info("============> Client Handler: channel received, e:", exception);
    }
}
