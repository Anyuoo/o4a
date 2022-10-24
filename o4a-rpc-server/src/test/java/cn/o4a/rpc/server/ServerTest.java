package cn.o4a.rpc.server;

import cn.o4a.rpc.common.LogChannelHandler;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class ServerTest {
    private static final Logger logger = LoggerFactory.getLogger(ServerTest.class);

    @Test
    void runSever9999() {
        try (final Server server = Server.bind(new InetSocketAddress("127.0.0.1", 9999), new LogChannelHandler())) {
            //
            logger.info("服务端已启动, instance: {}", server);
            TimeUnit.DAYS.sleep(1);
        } catch (Exception e) {
            //
        }
    }

    @Test
    void runSever9998() {
        try (final Server server = Server.bind(new InetSocketAddress("127.0.0.1", 9998), new LogChannelHandler())) {
            //
            logger.info("服务端已启动, instance: {}", server);
            TimeUnit.DAYS.sleep(1);
        } catch (Exception e) {
            //
            Thread.currentThread().interrupt();
        }
    }
}