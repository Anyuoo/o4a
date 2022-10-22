package cn.o4a.rpc.client;

import cn.o4a.rpc.common.Constants;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/22 12:52
 */
public class ClientConfiguration {
    /**
     * 空闲超时时间(mill)
     */
    private int idleTimeout = 1000;
    /**
     * 连接超时(mill)
     */
    private int connectTimeout = 5000;
    /**
     * 最大body大小
     */
    private int maxBodySize = 1024 * 64;

    /**
     * event 线程数
     */
    private int eventThreads = Constants.DEFAULT_IO_THREADS;

    public static ClientConfiguration defaultConfig() {
        return new ClientConfiguration();
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getMaxBodySize() {
        return maxBodySize;
    }

    public void setMaxBodySize(int maxBodySize) {
        this.maxBodySize = maxBodySize;
    }

    public int getEventThreads() {
        return eventThreads;
    }

    public void setEventThreads(int eventThreads) {
        this.eventThreads = eventThreads;
    }
}
