package cn.o4a.rpc.server;

import cn.newrank.niop.sdk.model.ConsumerMessage;
import cn.o4a.rpc.common.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 20:37
 */
public class SeverBizHandler implements ChannelHandler {
    private static final Logger logger = LoggerFactory.getLogger(SeverBizHandler.class);

    private final TaskCompleteHandler taskCompleteHandler;
    private final AbilityDispatcher abilityDispatcher;


    public SeverBizHandler(TaskCompleteHandler taskCompleteHandler, AbilityDispatcher abilityDispatcher) {
        this.taskCompleteHandler = taskCompleteHandler;
        this.abilityDispatcher = abilityDispatcher;
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        logger.info("============> Sever Handler: channel disconnected");
        abilityDispatcher.unregisterAbility(channel);
    }

    @Override
    public void connected(Channel channel) throws RemotingException {
        logger.info("============> Sever Handler: channel connected");
    }

    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        if (message.getStatus() == Message.STATUS_ERROR) {

            return;
        }
        final MessageCode code = message.getCode();
        logger.info("============> Sever Handler: channel received,  code:{}", code);
        if (code == MessageCode.TASK_COMPLETED) {
            //
            final JSONObject body = (JSONObject)message.getBody();
            final ConsumerMessage consumerMessage = body.toJavaObject(ConsumerMessage.class);
            taskCompleteHandler.complete(consumerMessage);
        } else {
            switch (code) {
                case NORMAL:

                    break;
                case REGISTER:
                    final JSONArray messageBody =  (JSONArray)message.getBody();
                    final List<String> abilityIds = messageBody.toJavaList(String.class);
                    abilityDispatcher.registerAbility(channel, new HashSet<>(abilityIds));
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
