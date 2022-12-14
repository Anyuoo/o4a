package cn.o4a.rpc.server;

import cn.newrank.niop.sdk.model.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class ServerTest {
    private static final Logger logger = LoggerFactory.getLogger(ServerTest.class);


    private static final AbilityDispatcher dispatcher = new AbilityDispatcher();
    private static final SeverBizHandler severBizHandler = new SeverBizHandler(consumerMessage -> {
        //任務出來
        logger.info("task completed, msg: {}", consumerMessage);
    }, dispatcher);
    private static final  Datasorce datasorce = new Datasorce(dispatcher);





    @Test
    void runSever9999() {
        try (final Server server = Server.bind(new InetSocketAddress("127.0.0.1", 9999), severBizHandler)) {
            //
            logger.info("服务端已启动, instance: {}", server);
            TimeUnit.DAYS.sleep(1);
        } catch (Exception e) {
            //
        }
    }

    @Test
    void runSever9998() {
        try (final Server server = Server.bind(new InetSocketAddress("127.0.0.1", 9998), severBizHandler)) {
            //
            logger.info("服务端已启动, instance: {}", server);
            //模拟任务提交
            datasorce.submit();
            TimeUnit.DAYS.sleep(1);
        } catch (Exception e) {
            //
            logger.error("sever error: ",e);
            Thread.currentThread().interrupt();
        }
    }



}