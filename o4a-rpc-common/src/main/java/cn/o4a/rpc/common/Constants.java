package cn.o4a.rpc.common;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/20 15:24
 */
public interface Constants {
    /**
     * 默认 IO 线程
     */
    int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);
    /**
     * 共享想线程池线程数
     */
    int SHARED_THREADS = 200;
    /**
     * 共享想线程池名称
     */
    String SHARED_EXECUTOR = "SHARED_EXECUTOR";
    /**
     * 私有线程前缀
     */
    String PRIVATE_EXECUTOR_PREFIX = "PRIVATE_EXECUTOR_";
    /**
     * channel key: 服务端地址
     */
    String CHANNEL_KEY_SEVER_ADDRESS = "SEVER_ADDRESS";
    /**
     * channel key: 重连次数
     */
    String CHANNEL_KEY_RECONNECT_TIMES = "RECONNECT_TIMES";


    Constants SELF = new Constants() {
    };

    default Constants self() {
        return SELF;
    }
}
