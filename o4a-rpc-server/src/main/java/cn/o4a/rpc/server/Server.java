package cn.o4a.rpc.server;

import cn.o4a.rpc.common.ChannelHandler;
import cn.o4a.rpc.common.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/17 19:17
 */
public class Server implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    /**
     * 当前Server本地地址
     */
    private final InetSocketAddress localAddress;
    private final ServerHandler serverHandler;
    private final ServerConfiguration configuration;
    private Map<String, Channel> channels;
    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private ChannelFuture channelFuture;

    private Server(InetSocketAddress localAddress, ChannelHandler handler, ServerConfiguration configuration) throws ConnectException {
        if (localAddress == null) {
            throw new IllegalArgumentException("localAddress == null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        if (configuration == null) {
            throw new IllegalArgumentException("configuration == null");
        }

        this.localAddress = localAddress;
        this.configuration = configuration;
        this.serverHandler = new ServerHandler(new HeartBeatHandler(new PrivateChannelHandler(handler)));
        initAndStart();
    }

    public static Server bind(InetSocketAddress address, ChannelHandler handler) throws ConnectException {
        return bind(address, handler, ServerConfiguration.defaultConfig());
    }

    public static Server bind(InetSocketAddress address, ChannelHandler handler, ServerConfiguration configuration) throws ConnectException {
        return new Server(address, handler, configuration);
    }

    private void initBootstrap() {
        //设置两个线程组boosGroup和workerGroup
        bootstrap.group(bossGroup, workerGroup)
                //设置服务端通道实现类型
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                //不等待
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //设置保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler())
                //使用匿名内部类的形式初始化通道对象
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                //帧解码器
                                .addLast("frame-decoder", new MessageFrameDecoder(configuration.getMaxBodySize()))
                                //消息编解码器
                                .addLast("message-codec", new MessageCodec())
                                //超时处理
                                .addLast("server-idle-handler", idlestatehandler())
                                //业务handler
                                .addLast("sever-handler", serverHandler);
                    }
                });
    }

    /**
     * 空闲超时连接处理器
     *
     * @return 空闲超时连接处理器
     */
    private IdleStateHandler idlestatehandler() {
        return new IdleStateHandler(0, 0, configuration.getHeartBeatTimeout(), MILLISECONDS);
    }

    private void initAndStart() throws ConnectException {
        //创建两个线程组 boosGroup、workerGroup
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(configuration.getWorkerThreads());
        //创建服务端的启动对象，设置参数
        bootstrap = new ServerBootstrap();
        initBootstrap();
        //绑定端口号，启动服务端
        channelFuture = bootstrap.bind(localAddress);
        channel = channelFuture.channel();
    }

    @Override
    public void close() {
        try {
            if (channel != null) {
                // unbind.
                channel.close();
            }
        } catch (Throwable e) {
            logger.info("Server channel close fail", e);
        }

        for (Channel channelUnit : getChannels()) {
            try {
                channelUnit.close();
            } catch (Throwable e) {
                logger.info("Client channel close fail", e);
            }
        }

        try {
            if (bootstrap != null) {
                long timeout = configuration.getShutdownTimeout();
                long quietPeriod = Math.min(2000L, timeout);
                Future<?> bossGroupShutdownFuture = bossGroup.shutdownGracefully(quietPeriod, timeout, MILLISECONDS);
                Future<?> workerGroupShutdownFuture = workerGroup.shutdownGracefully(quietPeriod, timeout, MILLISECONDS);
                bossGroupShutdownFuture.syncUninterruptibly();
                workerGroupShutdownFuture.syncUninterruptibly();
            }
        } catch (Throwable e) {
            //
        }
        try {
            if (channels != null) {
                channels.clear();
            }
        } catch (Throwable e) {
            //
        }
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    private Collection<Channel> getChannels() {
        return channels == null ? new ArrayList<>() : channels.values();
    }
}
