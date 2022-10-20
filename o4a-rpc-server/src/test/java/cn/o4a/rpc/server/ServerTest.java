package cn.o4a.rpc.server;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class ServerTest {
    private static final Logger logger = LoggerFactory.getLogger(ServerTest.class);

    @Test
    void run() {
        try (final Server server = Server.bind(new InetSocketAddress("127.0.0.1", 9999), null)) {
            //
            logger.info(server.toString());
            TimeUnit.DAYS.sleep(1);
        } catch (Exception e) {
            //
            Thread.currentThread().interrupt();
        }
    }
}