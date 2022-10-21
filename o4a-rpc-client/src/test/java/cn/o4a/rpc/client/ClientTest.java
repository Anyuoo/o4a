package cn.o4a.rpc.client;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class ClientTest {

    @Test
    void start() {
        try (final Client client = Client.connect(new InetSocketAddress("127.0.0.1", 9999), new ClientHandler())) {
            System.out.println(client);
            TimeUnit.DAYS.sleep(1);
        } catch (Exception e) {

        }

    }
}