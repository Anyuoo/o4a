package cn.o4a.rpc.client;

import cn.o4a.rpc.common.ChannelHandler;
import cn.o4a.rpc.common.HandlerWrappers;
import cn.o4a.rpc.common.LogChannelHandler;

import java.io.Closeable;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/24 17:02
 */
public class ClientCluster implements Closeable {

    private static final Map<InetSocketAddress, Client> CLIENT_MAP = new ConcurrentHashMap<>();
    private final ClientHandler clientHandler;

    public ClientCluster(ChannelHandler channelHandler) {
        this.clientHandler = new ClientHandler(HandlerWrappers.wrap(new LogChannelHandler()));
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
    }

    public int getConnectedClientSize() {
        return CLIENT_MAP.size();
    }
}
