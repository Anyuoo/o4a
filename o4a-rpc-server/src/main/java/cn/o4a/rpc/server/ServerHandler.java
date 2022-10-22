package cn.o4a.rpc.server;

import cn.o4a.rpc.common.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/20 15:43
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private final Map<String, Channel> channels = new ConcurrentHashMap<>();
    cn.o4a.rpc.common.ChannelHandler channelHandler;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送过来的消息
        final Message message = (Message) msg;
        if (message.isHeartBeatMessage()) {
            final Message heartBeatMessage = Message.heartBeatEvent();
            //回执
            ctx.writeAndFlush(heartBeatMessage);
            return;
        }
        logger.info("收到客户端 {}, 发送的消息：{}, heart beat:{} ,request: {} ", ctx.channel().remoteAddress(), message, message.isHeartBeatMessage(), message.isRequest());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            final Channel channel = ctx.channel();
            if (channel != null) {
                channel.close();
                logger.info("未收到客户端心跳, 连接中断");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常，关闭通道
        logger.info("未收到客户端心跳, 连接中断");
        ctx.close();
    }
}
