package cn.o4a.rpc.client;

import cn.o4a.rpc.common.*;
import io.netty.channel.ChannelFutureListener;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 17:02
 */
public class ClientCluster implements Closeable {

    private static final Map<InetSocketAddress, Client> CLIENT_MAP = new ConcurrentHashMap<>();
    private final ClientHandler clientHandler;

    public ClientCluster(ChannelHandler channelHandler) {
        this.clientHandler = new ClientHandler(wrapHandler(channelHandler));
    }

    private ChannelHandler wrapHandler(ChannelHandler channelHandler) {
        //心跳 + 线程模型
        return new ReconnectHandler(new HeartBeatHandler(new AllSharedChannelHandler(channelHandler)), this);
    }

    /**
     * 获取或注册客户端
     *
     * @param serverAddress 服务端地址
     * @return 客户端
     */
    public Client getOrRegister(InetSocketAddress serverAddress) {
        if (serverAddress == null) {
            throw new IllegalArgumentException("serverAddress == null");
        }
        Client client = CLIENT_MAP.get(serverAddress);

        if (client == null || !client.isConnected()) {
            //移除
            if (client != null) {
                client.close();
            }
            CLIENT_MAP.remove(serverAddress);
            //重建连接
            final Client rf = Client.connect(serverAddress, clientHandler);
            rf.getChannelFuture().addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    CLIENT_MAP.put(serverAddress, rf);
                }
            });
            client = rf;
        }
        return client;
    }


    @Override
    public void close() {
        try {
            for (Client client : CLIENT_MAP.values()) {
                try {
                    client.close();
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }
        CLIENT_MAP.clear();
        //
        Executors.destroy(Constants.SHARED_EXECUTOR, 5, TimeUnit.SECONDS);
    }

    public int getConnectedClientSize() {
        return CLIENT_MAP.size();
    }
}
