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
        //设置线程组
        return new Bootstrap().group(eventExecutors)
                //设置客户端的通道实现类型
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, configuration.getConnectTimeout())
                //初始化通道
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        //添加客户端通道的处理器
                        socketChannel.pipeline()
                                //帧解码
                                .addLast("frame-decoder", new MessageFrameDecoder(configuration.getMaxBodySize()))
                                //消息编解码器
                                .addLast("message-codec", MessageCodec.INSTANCE)
                                //超时处理
                                .addLast("client-idle-handler", idlestatehandler())
                                //业务handler
                                .addLast("client-handler", clientHandler);
                    }
                });
    }

    private void doConnect() {
        //连接服务端
        channelFuture = bootstrap.connect(serverAddress);
        //启动重连监听器
        channelFuture.addListener((ChannelFutureListener) cf -> {
            if (cf.isSuccess()) {
                return;
            }
            cf.channel().eventLoop().schedule(() -> {
                logger.info("---------->reconnect .......");
                doConnect();
            }, configuration.getReconnectDelay(), MILLISECONDS);
        });
        //对通道关闭进行监听
        channel = channelFuture.channel();
    }


    /**
     * 空闲超时连接处理器
     *
     * @return 空闲超时连接处理器
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
                //关闭线程组
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
