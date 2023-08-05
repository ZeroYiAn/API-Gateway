package com.zero.gateway.socket;

import com.zero.gateway.session.defaults.DefaultGatewaySessionFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

/**
 * @description: 网关网络通信服务：  创建Netty服务端
 * GatewaySocketServer 实现Callable接口，重写call方法，在call方法中创建Netty服务端，让Netty服务端在线程池中启动，不占用主线程
 * @author: ZeroYiAn
 * @time: 2023/8/3
 *
 * GatewaySocketServer是用于启动Netty服务的，相当于你启动Socket 的过程。而Netty的启动配置除了端口外，
 * 还需要把对应数据的处理一并初始化到Channel管道中，也就是对应的 GatewayChannelInitializer类的功能。
 *
 * 1.服务创建
 * Netty本身就是一个Socket NIO的包装，所以也要包括对服务的启动处理，这里我们实现Callable 接口，让服务在线程池中启动。
 */

public class GatewaySocketServer implements Callable<Channel> {

    private final Logger logger = LoggerFactory.getLogger(GatewaySocketServer.class);

    private DefaultGatewaySessionFactory gatewaySessionFactory;

    //负载等待连接
    private final EventLoopGroup boss = new NioEventLoopGroup(1);
    //负责数据处理：IO读写操作
    private final EventLoopGroup work = new NioEventLoopGroup();
    private Channel channel;

    public GatewaySocketServer(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture = null;
        try {
            //创建Netty服务端过程：
            //1.创建ServerBootstrap实例
            ServerBootstrap b = new ServerBootstrap();
            //2.设置并绑定Reactor线程池
            b.group(boss, work)
                    //3.设置并绑定服务端channel
                    .channel(NioServerSocketChannel.class)
                    //4.为引导类设置相应参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //5.设置childHandler（自定义的处理器就在其中），添加会话初始信息
                    .childHandler(new GatewayChannelInitializer(gatewaySessionFactory));
            //6.当上述信息创建完成，绑定启动监听端口
            channelFuture = b.bind(new InetSocketAddress(7397)).syncUninterruptibly();
            //7.启动线程后，就获取并返回Channel对象，对请求进行处理
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            logger.error("socket server start error.", e);
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                logger.info("socket server start done.");
            } else {
                logger.error("socket server start error.");
            }
        }
        return channel;
    }

}
