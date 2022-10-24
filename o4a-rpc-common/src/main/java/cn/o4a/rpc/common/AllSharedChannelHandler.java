package cn.o4a.rpc.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import static cn.o4a.rpc.common.ChannelEventRunnable.ChannelState;
/**
 * 线程模型
 *
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 19:11
 */
public class AllSharedChannelHandler extends ChannelHandlerDelegate{


    public AllSharedChannelHandler(ChannelHandler channelHandler) {
        super(channelHandler);
    }

    @Override
    public void connected(Channel channel) throws RemotingException {
        final ExecutorService sharedExecutor = Executors.sharedExecutor();
        try {
            sharedExecutor.execute(new ChannelEventRunnable(channel, channelHandler, ChannelState.CONNECTED));
        } catch (Throwable t) {
            throw new ExecutionException("connect event", channel, getClass() + " error when process connected event .", t);
        }
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        final ExecutorService sharedExecutor = Executors.sharedExecutor();
        try {
            sharedExecutor.execute(new ChannelEventRunnable(channel, channelHandler, ChannelState.DISCONNECTED));
        } catch (Throwable t) {
            throw new ExecutionException("disconnect event", channel, getClass() + " error when process disconnected event .", t);
        }
    }

    @Override
    public void received(Channel channel, Message message) throws RemotingException {
        final ExecutorService sharedExecutor = Executors.sharedExecutor();
        try {
            sharedExecutor.execute(new ChannelEventRunnable(channel, channelHandler, ChannelState.RECEIVED, message));
        } catch (Throwable t) {
            if(message.isRequest() && t instanceof RejectedExecutionException){
                sendFeedback(channel, message, t);
            }else {
                throw new ExecutionException(message, channel, getClass() + " error when process received event .", t);
            }
        }
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        final ExecutorService sharedExecutor = Executors.sharedExecutor();
        try {
            sharedExecutor.execute(new ChannelEventRunnable(channel, channelHandler, ChannelState.CAUGHT, exception));
        } catch (Throwable t) {
            throw new ExecutionException("caught event", channel, getClass() + " error when process caught event .", t);
        }
    }

    protected void sendFeedback(Channel channel, Message message, Throwable t) throws RemotingException {
        if (message.isEvent()) {
            return;
        }

        String msg = "Server side(" + channel.getRemoteAddress()
                + ") thread pool is exhausted, detail msg:" + t.getMessage();

        final Message response = Message.response(message.getId(), Message.STATUS_ERROR, null);
        response.setErrorMessage(msg);

        channel.send(response);
    }
}
