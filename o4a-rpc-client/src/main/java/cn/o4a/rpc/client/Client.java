package cn.o4a.rpc.client;

import cn.o4a.rpc.common.MessageCodec;
import cn.o4a.rpc.common.MessageFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.net.InetSocketAddress;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/20 9:10
 */
public class Client implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private final InetSocketAddress serverAddress;
    private final ClientHandler clientHandler;
    private final ClientConfiguration configuration;
    private final Bootstrap bootstrap;
    private NioEventLoopGroup eventExecutors;
    private Channel channel;
    private ChannelFuture channelFuture;

    private Client(InetSocketAddress serverAddress, ClientHandler clientHandler, ClientConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration == null");
        }
        if (serverAddress == null) {
            throw new IllegalArgumentException("serverAddress == null");
        }
        if (clientHandler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.configuration = configuration;
        this.serverAddress = serverAddress;
        this.clientHandler = clientHandler;
        this.bootstrap = initClientBootstrap();
        doConnect();
    }

    public static Client connect(InetSocketAddress serverAddress, ClientHandler clientHandler) {
        return new Client(serverAddress, clientHandler, ClientConfiguration.defaultConfig());
    }

    public boolean isConnected() {
        return channel.isActive();
    }

    private Bootstrap initClientBootstrap() {
        eventExecutors = new NioEventLoopGroup(configuration.getEventThreads());
        //???????????????
        return new Bootstrap().group(eventExecutors)
                //????????????????????????????????????
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, configuration.getConnectTimeout())
                //???????????????
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        //?????????????????????????????????
                        socketChannel.pipeline()
                                //?????????
                                .addLast("frame-decoder", new MessageFrameDecoder(configuration.getMaxBodySize()))
                                //??????????????????
                                .addLast("message-codec", MessageCodec.INSTANCE)
                                //????????????
                                .addLast("client-idle-handler", idlestatehandler())
                                //??????handler
                                .addLast("client-handler", clientHandler);
                    }
                });
    }

    private void doConnect() {
        //???????????????
        channelFuture = bootstrap.connect(serverAddress);
        //?????????????????????
        channelFuture.addListener((ChannelFutureListener) cf -> {
            if (cf.isSuccess()) {
                return;
            }
            cf.channel().eventLoop().schedule(() -> {
                logger.info("---------->reconnect .......");
                doConnect();
            }, configuration.getReconnectDelay(), MILLISECONDS);
        });
        //???????????????????????????
        channel = channelFuture.channel();
    }


    /**
     * ???????????????????????????
     *
     * @return ???????????????????????????
     */
    private IdleStateHandler idlestatehandler() {
        return new IdleStateHandler(configuration.getHeartbeatInterval(), 0, 0, MILLISECONDS);
    }

    public ClientConfiguration getConfiguration() {
        return configuration;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    @Override
    public void close() {
        try {
            if (eventExecutors != null) {
                //???????????????
                eventExecutors.shutdownGracefully();
            }
        } catch (Throwable throwable) {

        }
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (Throwable throwable) {

        }
    }
}
