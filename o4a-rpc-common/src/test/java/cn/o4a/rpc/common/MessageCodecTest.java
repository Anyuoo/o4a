package cn.o4a.rpc.common;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

class MessageCodecTest {

    @Test
    void encode() {
        final EmbeddedChannel channel = new EmbeddedChannel(
                new FrameDecoder(1024),
                new MessageCodec()
        );


    }

    @Test
    void decode() {
    }
}