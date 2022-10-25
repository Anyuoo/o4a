package cn.o4a.rpc.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import static cn.o4a.rpc.common.ChannelEventRunnable.ChannelState;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/25 11:00
 */
public class PrivateChannelHandler extends AllSharedChannelHandler {

    public PrivateChannelHandler(ChannelHandler channelHandler) {
        super(channelHandler);
    }

    private ExecutorService getPrivateExecutor(Channel channel) {
        return Executors.getOrCreatePrivateExecutor(channel.getRemoteAddress().toString(), 5, 30);
    }


    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        final ExecutorService privateExecutor = getPrivateExecutor(channel);
        try {
            privateExecutor.execute(new ChannelEventRunnable(channel, channelHandler, ChannelState.RECEIVED, message));
        } catch (Throwable t) {
            if (message.isRequest() && t instanceof RejectedExecutionException) {
                sendFeedback(channel, message, t);
            } else {
                throw new ExecutionException(message, channel, getClass() + " error when process received event .", t);
            }
        }
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        final ExecutorService privateExecutor = getPrivateExecutor(channel);
        try {
            privateExecutor.execute(new ChannelEventRunnable(channel, channelHandler, ChannelState.CAUGHT, exception));
        } catch (Throwable t) {
            throw new ExecutionException("caught event", channel, getClass() + " error when process caught event .", t);
        }
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return this;
    }
}
