package cn.o4a.rpc.client;

import cn.o4a.rpc.common.HandlerWrappers;
import cn.o4a.rpc.common.LogChannelHandler;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class ClientTest {

    @Test
    void start() {
        final ClientHandler clientHandler = new ClientHandler(HandlerWrappers.wrap(new LogChannelHandler()));
        try (final Client client = Client.connect(new InetSocketAddress("127.0.0.1", 9999), clientHandler)) {
            System.out.println(client);
            TimeUnit.DAYS.sleep(1);
        } catch (Exception e) {

        }
    }

    @Test
    void startC() {
        try (final ClientCluster clientCluster = new ClientCluster(new LogChannelHandler())) {

            final Client client1 = clientCluster.getOrRegister(new InetSocketAddress("127.0.0.1", 9999));
            final Client client2 = clientCluster.getOrRegister(new InetSocketAddress("127.0.0.1", 9998));

            TimeUnit.SECONDS.sleep(4);

            System.out.println("sever num: " + clientCluster.getConnectedClientSize());
            TimeUnit.DAYS.sleep(1);
        } catch (Exception e) {

        }
    }
}