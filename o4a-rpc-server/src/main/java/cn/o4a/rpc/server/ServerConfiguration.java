package cn.o4a.rpc.server;

import cn.o4a.rpc.common.Constants;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/20 15:35
 */
public class ServerConfiguration {
    /**
     * 可接受连接数
     */
    private int accepts = 128;
    /**
     * 空闲超时时间(mill)
     */
    private int idleTimeout = 1000;
    /**
     * 服务关闭超时(mill)
     */
    private int shutdownTimeout = 20000;
    /**
     * 最大body大小
     */
    private int maxBodySize = 1024 * 64;

    /**
     * worker 线程数
     */
    private int workerThreads = Constants.DEFAULT_IO_THREADS;

    public static ServerConfiguration defaultConfig() {
        return new ServerConfiguration();
    }

    public int getAccepts() {
        return accepts;
    }

    public void setAccepts(int accepts) {
        this.accepts = accepts;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public int getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(int shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public int getMaxBodySize() {
        return maxBodySize;
    }

    public void setMaxBodySize(int maxBodySize) {
        this.maxBodySize = maxBodySize;
    }
}
