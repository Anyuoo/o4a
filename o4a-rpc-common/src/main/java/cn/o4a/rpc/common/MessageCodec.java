package cn.o4a.rpc.common;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/21 17:08
 */
@ChannelHandler.Sharable
public final class MessageCodec extends AbstractMessageCodec<Message> {
    public static final MessageCodec INSTANCE = new MessageCodec();
    private static final Logger logger = LoggerFactory.getLogger(MessageCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> list) {
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        //魔数(2 byte)
        byteBuf.writeShort(MAGIC);
        //版本&消息类型(1 byte)
        byteBuf.writeByte(METADATA);
        //消息状态(1 byte)
        byteBuf.writeByte(message.getStatus());
        //消息id(8字节)
        byteBuf.writeLong(message.getId());
        if (message.isHeartBeatMessage()) {
            message.setBody(null);
        }
        //消息内容
        final byte[] messageBytes = JSON.toJSONBytes(message);
        //消息长度(4 byte)
        byteBuf.writeInt(messageBytes.length);
        //消息体
        byteBuf.writeBytes(messageBytes);
        list.add(byteBuf);
    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        final short magic = byteBuf.readShort();
        //
        final byte metadata = byteBuf.readByte();
        final byte msgStatus = byteBuf.readByte();
        final long msgId = byteBuf.readLong();
        final int msgLength = byteBuf.readInt();

        //
        Message message;
        try {
            final byte[] body = new byte[msgLength];
            byteBuf.readBytes(body, 0, msgLength);
            message = JSON.parseObject(body, Message.class);
        } catch (Exception e) {
            message = Message.exceptionEvent("MessageCodec#decode 异常, message: " + e.getMessage());
        }

        list.add(message);
    }
}
