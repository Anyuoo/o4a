package cn.o4a.rpc.server;

import cn.o4a.rpc.common.*;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 20:37
 */
public class BizHandler implements ChannelHandler {

    @Override
    public void disconnected(Channel channel) throws RemotingException {

    }


    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        if (message.getCode() == MessageCode.TASK_COMPLETED) {
            //

        }else {


        }
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {

    }
}
