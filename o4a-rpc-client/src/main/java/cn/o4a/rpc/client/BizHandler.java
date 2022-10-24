package cn.o4a.rpc.client;

import cn.o4a.rpc.common.Channel;
import cn.o4a.rpc.common.ChannelHandler;
import cn.o4a.rpc.common.Message;
import cn.o4a.rpc.common.RemotingException;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 11:24
 */
public class BizHandler implements ChannelHandler {


    @Override
    public void disconnected(Channel channel) throws RemotingException {
    }


    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        channel.send(null);

    }

    @Override
    public void caught(Channel channel, Throwable throwable) throws RemotingException {
        System.out.println("Exp:============>" + throwable);
    }
}
