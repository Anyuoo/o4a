package cn.o4a.rpc.server;

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
     * 空闲超时时间(seconds)
     */
    private int idleTimeout = 600;
    /**
     * 服务关闭超时(mill)
     */
    private int shutdownTimeout = 20000;
    /**
     * worker 线程数
     */
    private int workerThreads = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

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

}
