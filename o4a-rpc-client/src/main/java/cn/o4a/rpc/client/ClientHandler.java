package cn.o4a.rpc.client;

import cn.o4a.rpc.common.Message;
import cn.o4a.rpc.common.NettyChannel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/21 13:39
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private cn.o4a.rpc.common.ChannelHandler channelHandler;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        try {
            final NettyChannel nettyChannel = NettyChannel.getOrAddChannel(ctx.channel(), channelHandler);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端发送过来的消息
        final Message message = (Message) msg;
        //心跳消息
        if (message.isHeartBeatMessage()) {
            return;
        }
        logger.info("收到服务端 heart beat:{} ", message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("异常: ", cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            final Message heartBeatMessage = Message.heartBeatEvent();
            if (ctx.channel() == null || !ctx.channel().isOpen()) {
                return;
            }
            ctx.writeAndFlush(heartBeatMessage);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
