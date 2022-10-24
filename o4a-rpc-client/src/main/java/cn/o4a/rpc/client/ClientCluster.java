package cn.o4a.rpc.client;

import cn.o4a.rpc.common.*;

import java.io.Closeable;
import java.net.ConnectException;
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
        return new HeartBeatHandler(new AllSharedChannelHandler(channelHandler));
    }

    public Client getOrRegister(InetSocketAddress serverAddress) {
        if (serverAddress == null) {
            throw new IllegalArgumentException("serverAddress == null");
        }
        Client client = CLIENT_MAP.get(serverAddress);

        if (client == null) {
            try {
                client = Client.connect(serverAddress, clientHandler);
                if (client.isConnected()) {
                    CLIENT_MAP.put(serverAddress, client);
                }
            } catch (ConnectException connectException) {

            }
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
