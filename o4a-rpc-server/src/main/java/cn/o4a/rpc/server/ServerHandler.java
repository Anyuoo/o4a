package cn.o4a.rpc.server;

import cn.o4a.rpc.common.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
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
        Message message = (Message) msg;
        logger.info("收到客户端 {}, 发送的消息：{}", ctx.channel().remoteAddress(), message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //发送消息给客户端
        final Message response = Message.response(Message.STATUS_OK, "服务端已收到消息，并给你发送一个问号?");
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常，关闭通道
        ctx.close();
    }
}
