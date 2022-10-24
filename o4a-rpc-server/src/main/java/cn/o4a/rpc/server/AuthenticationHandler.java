package cn.o4a.rpc.server;

import cn.o4a.rpc.common.*;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 11:24
 */
public class AuthenticationHandler extends AbstractChannelHandler {

    public AuthenticationHandler(ChannelHandler channelHandler) {
        super(channelHandler);
    }


    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        super.received(channel, message);
    }
}
