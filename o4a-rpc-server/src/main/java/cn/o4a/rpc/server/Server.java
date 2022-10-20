package cn.o4a.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/17 19:17
 */
public class Server implements Closeable {
    private Map<String, Channel> channels;
    /**
     * 当前Server本地地址
     */
    private final InetSocketAddress localAddress;

    private final ChannelHandler handler;
    private final ServerConfiguration configuration;

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;

    public Server(InetSocketAddress localAddress,  ChannelHandler handler, ServerConfiguration configuration) {
        this.localAddress = localAddress;
        this.configuration = configuration;
        this.handler = handler;
        start();
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
                //使用匿名内部类的形式初始化通道对象
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                //.addLast("decoder", adapter.getDecoder())
                                //.addLast("encoder", adapter.getEncoder())
                                .addLast("server-idle-handler", idlestatehandler())
                                .addLast("handler", new Handler());
                    }
                });
    }

    /**
     * 空闲超时连接处理器
     *
     * @return 空闲超时连接处理器
     */
    private IdleStateHandler idlestatehandler() {
        return new IdleStateHandler(0, 0, configuration.getIdleTimeout(), MILLISECONDS);
    }


    public static void main(String[] args) {
        try (final Server server = Server.bind(new InetSocketAddress("127.0.0.1",9999), null);) {
            //
            System.out.println(server.channel);
            TimeUnit.DAYS.sleep(1);
        } catch (Exception e) {
            //
        }
    }


    private void start(){
        //创建两个线程组 boosGroup、workerGroup
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(configuration.getWorkerThreads());
        //创建服务端的启动对象，设置参数
        bootstrap = new ServerBootstrap();
        initBootstrap();
        //绑定端口号，启动服务端
        final ChannelFuture channelFuture = bootstrap.bind(localAddress);
        channel = channelFuture.channel();
    }

   public static Server bind(InetSocketAddress address, ChannelHandler handler) {
       return bind(address, handler,ServerConfiguration.defaultConfig());
   }

    public static Server bind(InetSocketAddress address, ChannelHandler handler, ServerConfiguration configuration) {
        return new Server(address, handler, configuration);
    }

    @Override
    public void close() {
        try {
            if (channel != null) {
                // unbind.
                channel.close();
            }
        } catch (Throwable e) {
           //
        }

        for (Channel channelUnit : getChannels()) {
            try {
                channelUnit.close();
            } catch (Throwable e) {
                //
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

    private Collection<Channel> getChannels() {
        return channels == null ? new ArrayList<>() : channels.values();
    }
}
