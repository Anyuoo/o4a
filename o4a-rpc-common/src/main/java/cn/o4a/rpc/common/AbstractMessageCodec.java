package cn.o4a.rpc.common;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageCodec;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/21 14:28
 */
public abstract class AbstractMessageCodec<T> extends MessageToMessageCodec<ByteBuf, T> {
    /**
     * 请求头长度
     */
    protected static final int HEADER_LENGTH = 16;
    /**
     * 魔数(2 byte)
     */
    protected static final short MAGIC = 777;
    protected static final byte MAIN_VERSION = 1;
    protected static final byte SUB_VERSION = 0;
    /**
     * 版本 & 类型
     */
    protected static final byte METADATA = 100;


    public static int messageSize(int bodySize) {
        return HEADER_LENGTH + bodySize;
    }

}
