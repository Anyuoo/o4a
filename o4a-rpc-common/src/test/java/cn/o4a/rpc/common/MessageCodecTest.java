package cn.o4a.rpc.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

class MessageCodecTest {

    @Test
    void encode() {
        final EmbeddedChannel channel = new EmbeddedChannel(
                new MessageFrameDecoder(1024),
                new MessageCodec()
        );


        final Message response = Message.response(0, Message.STATUS_OK, "HALLL", MessageCode.NORMAL);
        final Message request = Message.request("haasfdsf", MessageCode.NORMAL);
        //channel.writeOneOutbound(message);

        final ArrayList<Object> list = new ArrayList<>();
        MessageCodec.INSTANCE.encode(null, request, list);

        final ByteBuf o = (ByteBuf) list.get(0);
        MessageCodec.INSTANCE.decode(null, o, list);

        System.out.println();
        try {

            TimeUnit.DAYS.sleep(1);

        } catch (Exception e) {

        }
    }

    @Test
    void decode() {
    }
}