package cn.o4a.rpc.client;

import cn.o4a.rpc.common.Message;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/21 13:39
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelDuplexHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送消息到服务端
        ctx.writeAndFlush(Message.request("歪比巴卜~茉莉~Are you good~马来西亚~"));
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端发送过来的消息
        Message message = (Message) msg;
        System.out.println("收到服务端" + ctx.channel().remoteAddress() + "的消息：" + message);
        ctx.fireChannelRead(msg);
    }
}
