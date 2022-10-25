package cn.o4a.rpc.client;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class ClientTest {

    @Test
    void startC() {
        try (final ClientCluster clientCluster = new ClientCluster(new BizHandler())) {

            final Client client1 = clientCluster.getOrRegister(new InetSocketAddress("127.0.0.1", 9999));
            final Client client2 = clientCluster.getOrRegister(new InetSocketAddress("127.0.0.1", 9998));

            TimeUnit.SECONDS.sleep(4);

            System.out.println("sever num: " + clientCluster.getConnectedClientSize());
            TimeUnit.DAYS.sleep(1);
        } catch (Exception e) {

        }
    }
}