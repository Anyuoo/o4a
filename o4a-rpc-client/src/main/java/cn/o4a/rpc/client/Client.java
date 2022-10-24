package cn.o4a.rpc.client;

import cn.o4a.rpc.common.ChannelHandler;
import cn.o4a.rpc.common.HandlerWrappers;
import cn.o4a.rpc.common.MessageCodec;
import cn.o4a.rpc.common.MessageFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.Closeable;
import java.net.InetSocketAddress;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/10/20 9:10
 */
public class Client implements Closeable {

    private final InetSocketAddress serverAddress;
    private final ClientHandler clientHandler;
    private final ClientConfiguration configuration;
    private NioEventLoopGroup eventExecutors;
    private Channel channel;

    private Client(InetSocketAddress serverAddress, ChannelHandler handler, ClientConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration == null");
        }
        if (serverAddress == null) {
            throw new IllegalArgumentException("serverAddress == null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.configuration = configuration;
        this.serverAddress = serverAddress;
        this.clientHandler = new ClientHandler(HandlerWrappers.wrap(handler));
        initAndConnect();
    }

    public static Client connect(InetSocketAddress serverAddress, ChannelHandler handler) {
        return new Client(serverAddress, handler, ClientConfiguration.defaultConfig());
    }

    private void initAndConnect() {
        eventExecutors = new NioEventLoopGroup(configuration.getEventThreads());
        //创建bootstrap对象，配置参数
        final Bootstrap bootstrap = new Bootstrap();
        //设置线程组
        bootstrap.group(eventExecutors)
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
        //连接服务端
        final ChannelFuture channelFuture = bootstrap.connect(serverAddress);
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
