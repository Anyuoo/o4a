package cn.o4a.rpc.common;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 15:19
 */
public class HandlerWrappers {

    public static ChannelHandler wrap(ChannelHandler channelHandler) {
        return new HeartBeatHandler(channelHandler);
    }

    private void HandlerWrappers() {
    }
}
