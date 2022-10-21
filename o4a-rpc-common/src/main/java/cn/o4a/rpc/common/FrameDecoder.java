package cn.o4a.rpc.common;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/21 19:14
 */
public class FrameDecoder extends LengthFieldBasedFrameDecoder {


    public FrameDecoder(int maxMessageLength) {
        super(maxMessageLength, 12, 4, 0, 0);
    }
}
